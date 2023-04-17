package com.mborodin.uwm.services;

import static com.mborodin.uwm.api.enums.Role.ROLE_STUDENT;
import static com.mborodin.uwm.api.enums.Role.ROLE_TEACHER;
import static com.mborodin.uwm.security.UserContextHolder.getId;
import static com.mborodin.uwm.security.UserContextHolder.getLanguages;
import static com.mborodin.uwm.security.UserContextHolder.hasRole;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.AccessToFileApi;
import com.mborodin.uwm.api.FileApi;
import com.mborodin.uwm.api.SaveFileApi;
import com.mborodin.uwm.api.enums.FileType;
import com.mborodin.uwm.api.exceptions.filestorage.CouldNotLoadFileException;
import com.mborodin.uwm.api.exceptions.filestorage.CouldNotStoreAvatarException;
import com.mborodin.uwm.api.exceptions.filestorage.CouldNotStoreFileException;
import com.mborodin.uwm.model.persistence.AccessToFileDB;
import com.mborodin.uwm.model.persistence.FileDB;
import com.mborodin.uwm.model.persistence.SubjectDB;
import com.mborodin.uwm.model.persistence.UserDb;
import com.mborodin.uwm.repositories.AccessToFileRepository;
import com.mborodin.uwm.repositories.FileRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
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
        final String username = getId();
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
                log.error("Directory contains invalid symbols. Directory: {}", directory);
                throw new CouldNotStoreFileException(getLanguages(), file.getFile().getName());
            }

            if (Files.notExists(directory)) {
                Files.createDirectories(directory);
            }

            final Path targetLocation = directory.resolve(fileName);
            Files.copy(file.getFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            fileRepository.save(FileDB.builder()
                                      .name(fileName)
                                      .createDate(Instant.now())
                                      .subjectId(subjectDb.getId())
                                      .fileTypeId(file.getFileTypeId())
                                      .path(directory.toString())
                                      .build());
            return fileName;
        } catch (IOException e) {
            throw new CouldNotStoreFileException(getLanguages(), file.getFile().getName());
        }
    }

    public void updateAvatar(final MultipartFile avatar) {
        final Path targetLocation = userAvatarStorageLocation.resolve(getId() + ".jpg");

        try {
            Files.copy(avatar.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new CouldNotStoreAvatarException(getLanguages());
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
                        .forEach(groupId -> accessToFileRepository.save(AccessToFileDB.builder()
                                                                                      .studyGroupId(groupId)
                                                                                      .fileId(fileId)
                                                                                      .dateAddAccess(Instant.now())
                                                                                      .build())));
    }

    public List<FileApi> findAllFiles() {
        if (hasRole(ROLE_STUDENT)) {
            final Long studyGroupId = UserContextHolder.getGroupId();
            return findFilesByGroupId(studyGroupId);
        } else if (hasRole(ROLE_TEACHER)) {
            return subjectService.findUsersSubjects()
                                 .stream()
                                 .map(SubjectDB::getId)
                                 .map(fileRepository::findAllBySubjectId)
                                 .flatMap(List::stream)
                                 .map(fileDB -> FileApi.builder()
                                                       .fileId(fileDB.getId())
                                                       .fileName(fileDB.getName())
                                                       .type(fileDB.getFileTypeId())
                                                       .fileType(FileType.getById(fileDB.getFileTypeId()))
                                                       .subjectId(fileDB.getSubjectId())
                                                       .startAccessTime(fileDB.getCreateDate())
                                                       .build())
                                 .collect(Collectors.toList());
        }

        throw new UnsupportedOperationException();
    }

    public List<FileApi> findFilesByGroupId(final Long groupId) {
        return accessToFileRepository.findAllByStudyGroupId(groupId)
                                     .stream()
                                     .map(accessToFileDB -> fileRepository
                                             .findById(accessToFileDB.getFileId())
                                             .map(fileDb -> FileApi.builder()
                                                                   .fileId(accessToFileDB.getFileId())
                                                                   .fileName(fileDb.getName())
                                                                   .type(fileDb.getFileTypeId())
                                                                   .fileType(FileType.getById(fileDb.getFileTypeId()))
                                                                   .subjectId(fileDb.getSubjectId())
                                                                   .startAccessTime(accessToFileDB.getDateAddAccess())
                                                                   .build())
                                             .orElse(null))
                                     .filter(Objects::nonNull)
                                     .collect(Collectors.toList());
    }

    public FileApi findFileById(final Long fileId) {
        final var fileOptional = fileRepository.findById(fileId);

        if (fileOptional.isEmpty()) {
            log.warn("File with id {} does not exist.", fileId);
            return null;
        }

        final var file = fileOptional.get();
        final var subject = subjectService.findById(file.getSubjectId());

        return FileApi.builder()
                      .fileId(file.getId())
                      .fileName(file.getName())
                      .type(file.getFileTypeId())
                      .fileType(FileType.getById(file.getFileTypeId()))
                      .subjectId(file.getSubjectId())
                      .teacherId(subject.map(SubjectDB::getTeacher)
                                        .map(UserDb::getId)
                                        .orElse(null))
                      .build();
    }

    public void deleteFile(final FileApi file) {
        final Path directory = fileStorageLocation.resolve(String.valueOf(file.getSubjectId()))
                                                  .resolve(String.valueOf(file.getFileType().getId()));
        final Path filePath = directory.resolve(file.getFileName());

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        accessToFileRepository.deleteAllByFileId(file.getFileId());
        fileRepository.deleteById(file.getFileId());
    }

    private void createDirectorySuppressException(final Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception ex) {
            log.error("Could not create the directory with path {}", path);
            ex.printStackTrace();
        }
    }

    private Resource loadFileByPath(final Path path) {
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new CouldNotLoadFileException(getLanguages(), path.toString());
        }

        if (resource.exists()) {
            return resource;
        } else {
            return null;
        }
    }
}
