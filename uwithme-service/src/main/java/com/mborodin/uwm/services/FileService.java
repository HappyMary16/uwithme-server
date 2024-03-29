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

import javax.transaction.Transactional;

import com.mborodin.uwm.api.AccessToFileApi;
import com.mborodin.uwm.api.FileApi;
import com.mborodin.uwm.api.SubjectApi;
import com.mborodin.uwm.api.enums.FileType;
import com.mborodin.uwm.api.exceptions.filestorage.CouldNotLoadFileException;
import com.mborodin.uwm.api.exceptions.filestorage.CouldNotStoreAvatarException;
import com.mborodin.uwm.api.exceptions.filestorage.CouldNotStoreFileException;
import com.mborodin.uwm.model.persistence.AccessToFileDB;
import com.mborodin.uwm.model.persistence.FileDB;
import com.mborodin.uwm.repositories.AccessToFileRepository;
import com.mborodin.uwm.repositories.FileRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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

        try {
            Files.createDirectories(fileStorageLocation);
            Files.createDirectories(userAvatarStorageLocation);
        } catch (IOException ex) {
            log.error("Could not create the directories for files.", ex);
        }

        this.fileRepository = fileRepository;
        this.accessToFileRepository = accessToFileRepository;
        this.subjectService = subjectService;
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void moveFilesToBaseFileFolder() {
        for (final FileDB file : fileRepository.findAllByPathIsNotNull()) {
            log.info("Move and rename file {}", file);
            final Path filePath = fileStorageLocation.resolve(file.getPath()).resolve(file.getName()).normalize();

            final String extension = file.getName().substring(file.getName().lastIndexOf("."));
            final Path targetLocation = fileStorageLocation.resolve(file.getId() + extension);

            try {
                Files.move(filePath, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new CouldNotStoreFileException(getLanguages(), file.getName());
            }

            fileRepository.save(file.toBuilder()
                                    .path(null)
                                    .build());
        }
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void updateFilesOwners() {
        for (final FileDB file : fileRepository.findAllByOwnerIsNull()) {
            log.info("Update owner for file {}", file);
            fileRepository.save(file.toBuilder()
                                    .owner(subjectService.findById(file.getSubjectId())
                                                         .map(SubjectApi::getTeacherId)
                                                         .orElse(null))
                                    .build());
        }
    }

    public String saveFile(final MultipartFile file, final Long subjectId, final Integer fileType) {
        final String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        final var fileEntity = fileRepository.save(FileDB.builder()
                                                         .name(fileName)
                                                         .createDate(Instant.now())
                                                         .subjectId(subjectId)
                                                         .fileTypeId(fileType)
                                                         .owner(UserContextHolder.getId())
                                                         .build());

        try {
            final String extension = fileName.substring(fileName.lastIndexOf("."));
            final Path targetLocation = fileStorageLocation.resolve(fileEntity.getId() + extension);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            fileRepository.delete(fileEntity);
            throw new CouldNotStoreFileException(getLanguages(), file.getName());
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

    public void deleteAvatar() {
        final Path targetLocation = userAvatarStorageLocation.resolve(getId() + ".jpg");
        try {
            Files.delete(targetLocation);
        } catch (IOException e) {
            log.warn("Could not delete an avatar of user {}", getId());
        }
    }

    public Resource loadFile(final FileDB file) {
        final String extension = file.getName().substring(file.getName().lastIndexOf("."));
        final Path filePath = fileStorageLocation.resolve(file.getId() + extension).normalize();
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
            return fileRepository.findAllByOwner(UserContextHolder.getId())
                                 .stream()
                                 .map(fileDB -> FileApi.builder()
                                                       .fileId(fileDB.getId())
                                                       .fileName(fileDB.getName())
                                                       .type(fileDB.getFileTypeId())
                                                       .fileType(FileType.getById(fileDB.getFileTypeId()))
                                                       .subjectId(fileDB.getSubjectId())
                                                       .startAccessTime(fileDB.getCreateDate())
                                                       .teacherId(fileDB.getOwner())
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
                                                                   .teacherId(fileDb.getOwner())
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

        return FileApi.builder()
                      .fileId(file.getId())
                      .fileName(file.getName())
                      .type(file.getFileTypeId())
                      .fileType(FileType.getById(file.getFileTypeId()))
                      .subjectId(file.getSubjectId())
                      .teacherId(file.getOwner())
                      .build();
    }

    @Transactional
    public void deleteFile(final FileApi file) {
        final String extension = file.getFileName().substring(file.getFileName().lastIndexOf("."));
        final Path filePath = fileStorageLocation.resolve(file.getFileId() + extension).normalize();

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            log.warn("Could not delete file. File name: {}, id: {}", file.getFileName(), file.getFileId(), e);
        }

        accessToFileRepository.deleteAllByFileId(file.getFileId());
        fileRepository.deleteById(file.getFileId());
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
