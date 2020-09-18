package com.educationapp.server.services;

import static com.educationapp.server.enums.Role.STUDENT;
import static com.educationapp.server.enums.Role.TEACHER;
import static java.lang.String.format;

import java.io.FileNotFoundException;
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

import com.educationapp.server.exception.FileStorageException;
import com.educationapp.server.models.api.AccessToFileApi;
import com.educationapp.server.models.api.FileApi;
import com.educationapp.server.models.api.SaveFileApi;
import com.educationapp.server.models.api.UpdateAvatarApi;
import com.educationapp.server.models.persistence.AccessToFileDB;
import com.educationapp.server.models.persistence.FileDB;
import com.educationapp.server.models.persistence.SubjectDB;
import com.educationapp.server.models.persistence.UserDB;
import com.educationapp.server.repositories.AccessToFileRepository;
import com.educationapp.server.repositories.FileRepository;
import com.educationapp.server.repositories.StudentRepository;
import com.educationapp.server.repositories.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FileService {

    private final Path fileStorageLocation;
    private final Path userAvatarStorageLocation;
    private final FileRepository fileRepository;
    private final AccessToFileRepository accessToFileRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final SubjectService subjectService;

    @Autowired
    public FileService(final FileRepository fileRepository,
                       final AccessToFileRepository accessToFileRepository,
                       final UserRepository userRepository,
                       final StudentRepository studentRepository,
                       final SubjectService subjectService,
                       @Value("${file.upload.directory}") final String fileUploadDirectory,
                       @Value("${user.avatar.upload.directory}") final String userAvatarUploadDirectory) {
        fileStorageLocation = Paths.get("D:\\Programming\\Projects\\EducationAppServer")
                                   .resolve(fileUploadDirectory)
                                   .toAbsolutePath()
                                   .normalize();
        userAvatarStorageLocation = Paths.get("D:\\Programming\\Projects\\EducationAppServer")
                                         .resolve(userAvatarUploadDirectory)
                                         .toAbsolutePath()
                                         .normalize();

        createDirectorySuppressException(fileStorageLocation);
        createDirectorySuppressException(userAvatarStorageLocation);

        this.fileRepository = fileRepository;
        this.accessToFileRepository = accessToFileRepository;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.subjectService = subjectService;
    }

    public String saveFile(final SaveFileApi file) {
        final String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getFile().getOriginalFilename()));
        SubjectDB subjectDb = subjectService.findSubjectsByTeacherUsername(file.getUsername())
                                            .stream()
                                            .filter(subject -> subject.getName().equals(file.getSubjectName()))
                                            .findFirst()
                                            .orElse(null);

        if (Objects.isNull(subjectDb)) {
            subjectDb = subjectService.save(file.getUsername(), file.getSubjectName());
        }

        final String fileLocation = subjectDb.getId() + "\\" + file.getFileTypeId() + "\\";

        try {
            if (fileLocation.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileLocation);
            }

            Path directory = fileStorageLocation.resolve(fileLocation);
            if (Files.notExists(directory)) {
                Files.createDirectories(directory);
            }

            Path targetLocation = fileStorageLocation.resolve(fileLocation.concat(fileName));
            Files.copy(file.getFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            fileRepository.save(new FileDB(fileLocation,
                                           fileName,
                                           subjectDb.getId(),
                                           file.getFileTypeId()));
            return fileLocation;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + fileLocation + ". Please try again!", e);
        }
    }

    public void updateAvatar(final UpdateAvatarApi updateAvatarApi) {
        final Path targetLocation = userAvatarStorageLocation.resolve(updateAvatarApi.getUserId().toString());

        try {
            Files.copy(updateAvatarApi.getFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException(format("Could not store avatar for user %s. Please try again!",
                                                  updateAvatarApi.getUserId()), e);
        }
    }

    public Resource loadAvatar(final Long userId) {
        final Path filePath = userAvatarStorageLocation.resolve(userId.toString());
        return loadFileByPath(filePath);
    }

    public Resource loadFile(final FileDB file) {
        Path filePath = fileStorageLocation.resolve(file.getPath() + file.getName()).normalize();
        return loadFileByPath(filePath);
    }

    public void saveAccessToFile(final AccessToFileApi accessToFileApi) {
        accessToFileApi
                .getFileIds()
                .forEach(fileId -> accessToFileApi
                        .getGroupIds()
                        .forEach(groupId -> accessToFileRepository.save(new AccessToFileDB(groupId,
                                                                                           fileId,
                                                                                           new Date()))));
    }

    public List<FileApi> findByUsernameAndSubjectName(final String username, final Long subjectId) {
        final UserDB user = userRepository.findByUsername(username).get();
        if (user.getRole().equals(STUDENT.getId())) {
            final Long studyGroupId = studentRepository.findById(user.getId()).get().getStudyGroupId();
            return accessToFileRepository.findAllByStudyGroupId(studyGroupId)
                                         .stream()
                                         .map(accessToFileDB -> {
                                             final FileDB fileDB =
                                                     fileRepository.findById(accessToFileDB.getFileId()).get();
                                             return new FileApi(accessToFileDB.getFileId(),
                                                                fileDB.getName(),
                                                                fileDB.getFileTypeId(),
                                                                fileDB.getSubjectId(),
                                                                accessToFileDB.getDateAddAccess());
                                         })
                                         .filter(file -> file.getSubjectId().equals(subjectId))
                                         .collect(Collectors.toList());
        } else if (user.getRole().equals(TEACHER.getId())) {
            return fileRepository.findAllBySubjectId(subjectId)
                                 .stream()
                                 .map(fileDB ->
                                              new FileApi(fileDB.getId(),
                                                          fileDB.getName(),
                                                          fileDB.getFileTypeId(),
                                                          fileDB.getSubjectId(),
                                                          fileDB.getCreateDate()))
                                 .collect(Collectors.toList());
        }

        throw new UnsupportedOperationException();
    }

    public List<FileApi> findByUsername(final String username) {
        final UserDB user = userRepository.findByUsername(username).get();
        if (user.getRole().equals(STUDENT.getId())) {
            final Long studyGroupId = studentRepository.findById(user.getId()).get().getStudyGroupId();
            return accessToFileRepository.findAllByStudyGroupId(studyGroupId)
                                         .stream()
                                         .map(accessToFileDB -> {
                                             final FileDB fileDB =
                                                     fileRepository.findById(accessToFileDB.getFileId()).get();
                                             return new FileApi(accessToFileDB.getFileId(),
                                                                fileDB.getName(),
                                                                fileDB.getFileTypeId(),
                                                                fileDB.getSubjectId(),
                                                                accessToFileDB.getDateAddAccess());
                                         })
                                         .collect(Collectors.toList());
        } else if (user.getRole().equals(TEACHER.getId())) {
            return subjectService.findSubjectsByTeacherUsername(username)
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

    private void createDirectorySuppressException(final Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception ex) {
            throw new FileStorageException(format("Could not create the directory with path %s", path), ex);
        }
    }

    @SneakyThrows
    private Resource loadFileByPath(final Path path) {
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException(format("File with path %s not found", path));
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException(format("File with path %s not found", path));
        }
    }
}
