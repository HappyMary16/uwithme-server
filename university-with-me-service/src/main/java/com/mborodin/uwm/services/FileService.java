package com.mborodin.uwm.services;

import static com.mborodin.uwm.enums.Role.STUDENT;
import static com.mborodin.uwm.enums.Role.TEACHER;
import static java.lang.String.format;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.AccessToFileApi;
import com.mborodin.uwm.api.FileApi;
import com.mborodin.uwm.api.SaveFileApi;
import com.mborodin.uwm.enums.Role;
import com.mborodin.uwm.exception.FileStorageException;
import com.mborodin.uwm.models.persistence.AccessToFileDB;
import com.mborodin.uwm.models.persistence.FileDB;
import com.mborodin.uwm.models.persistence.SubjectDB;
import com.mborodin.uwm.repositories.AccessToFileRepository;
import com.mborodin.uwm.repositories.FileRepository;
import com.mborodin.uwm.security.UserContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileService {

    private final Path fileStorageLocation;
    private final Path userAvatarStorageLocation;
    private final FileRepository fileRepository;
    private final AccessToFileRepository accessToFileRepository;
    private final SubjectService subjectService;

    public FileService(final FileRepository fileRepository,
                       final AccessToFileRepository accessToFileRepository,
                       final SubjectService subjectService,
                       @Value("${main.upload.directory}") final String mainDirectory,
                       @Value("${files.upload.directory}") final String filesUploadDirectory,
                       @Value("${avatars.upload.directory}") final String avatarsUploadDirectory) {
        fileStorageLocation = Paths.get(mainDirectory)
                                   .toAbsolutePath()
                                   .resolve(filesUploadDirectory);
        userAvatarStorageLocation = Paths.get(mainDirectory)
                                         .toAbsolutePath()
                                         .resolve(avatarsUploadDirectory);

        createDirectorySuppressException(fileStorageLocation);
        createDirectorySuppressException(userAvatarStorageLocation);

        this.fileRepository = fileRepository;
        this.accessToFileRepository = accessToFileRepository;
        this.subjectService = subjectService;
    }

    public String saveFile(final SaveFileApi file) {
        final String username = UserContextHolder.getId();
        final String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getFile().getOriginalFilename()));

        final SubjectDB subjectDb = subjectService.findUsersSubjects()
                                                  .stream()
                                                  .filter(subject -> subject.getName().equals(file.getSubjectName()))
                                                  .findFirst()
                                                  .orElseGet(
                                                          () -> subjectService.save(username, file.getSubjectName()));

        try {
            final Path directory = fileStorageLocation.resolve(subjectDb.getId().toString())
                                                      .resolve(file.getFileTypeId().toString());

            if (directory.toString().contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + directory);
            }

            if (Files.notExists(directory)) {
                Files.createDirectories(directory);
            }

            final Path targetLocation = directory.resolve(fileName);
            Files.copy(file.getFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            fileRepository.save(new FileDB(directory.toString(),
                                           fileName,
                                           subjectDb.getId(),
                                           file.getFileTypeId()));
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + file.getFile().getName() + ". Please try again!",
                                           e);
        }
    }

    public void updateAvatar(final MultipartFile avatar) {
        final String userId = UserContextHolder.getId();
        final Path targetLocation = userAvatarStorageLocation.resolve(userId + ".jpg");

        try {
            Files.copy(avatar.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException(format("Could not store avatar for user %s. Please try again!", userId), e);
        }
    }

    public Resource loadAvatar(final String userId) {
        final Path filePath = userAvatarStorageLocation.resolve(userId + ".jpg");
        return loadFileByPath(filePath);
    }

    public Resource loadFile(final FileDB file) {
        final Path filePath = fileStorageLocation.resolve(file.getPath()).resolve(file.getName()).normalize();
        return loadFileByPath(filePath);
    }

    public void addAccessToFile(final AccessToFileApi accessToFileApi) {
        accessToFileApi
                .getFileIds()
                .forEach(fileId -> accessToFileApi
                        .getGroupIds()
                        .forEach(groupId -> accessToFileRepository.save(new AccessToFileDB(groupId,
                                                                                           fileId,
                                                                                           new Date()))));
    }

    public List<FileApi> findAllFiles() {
        final Role userRole = UserContextHolder.getRole();
        if (STUDENT.equals(userRole)) {
            final Long studyGroupId = UserContextHolder.getGroupId();
            return findFilesByGroupId(studyGroupId);
        } else if (TEACHER.equals(userRole)) {
            return subjectService.findUsersSubjects()
                                 .stream()
                                 .map(SubjectDB::getId)
                                 .map(fileRepository::findAllBySubjectId)
                                 .flatMap(List::stream)
                                 .map(fileDB -> new FileApi(fileDB.getId(),
                                                            fileDB.getName(),
                                                            fileDB.getFileTypeId(),
                                                            fileDB.getSubjectId(),
                                                            fileDB.getCreateDate()))
                                 .collect(Collectors.toList());
        }

        throw new UnsupportedOperationException();
    }

    public List<FileApi> findFilesByGroupId(final Long groupId) {
        return accessToFileRepository.findAllByStudyGroupId(groupId)
                                     .stream()
                                     .map(accessToFileDB -> fileRepository.findById(accessToFileDB.getFileId())
                                                                          .map(fileDb -> new FileApi(
                                                                                  accessToFileDB.getFileId(),
                                                                                  fileDb.getName(),
                                                                                  fileDb.getFileTypeId(),
                                                                                  fileDb.getSubjectId(),
                                                                                  accessToFileDB.getDateAddAccess()))
                                                                          .orElse(null))
                                     .filter(Objects::nonNull)
                                     .collect(Collectors.toList());
    }

    private void createDirectorySuppressException(final Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception ex) {
            throw new FileStorageException(format("Could not create the directory with path %s", path), ex);
        }
    }

    private Resource loadFileByPath(final Path path) {
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new FileStorageException(format("Could not load file with path %s", path), e);
        }

        if (resource.exists()) {
            return resource;
        } else {
            return null;
        }
    }
}
