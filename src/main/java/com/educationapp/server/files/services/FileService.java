package com.educationapp.server.files.services;

import static com.educationapp.server.common.enums.Role.STUDENT;
import static com.educationapp.server.common.enums.Role.TEACHER;

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

import com.educationapp.server.common.api.AccessToFileApi;
import com.educationapp.server.common.api.FileApi;
import com.educationapp.server.common.api.SaveFileApi;
import com.educationapp.server.common.exception.FileStorageException;
import com.educationapp.server.files.models.persistence.AccessToFileDB;
import com.educationapp.server.files.models.persistence.FileDB;
import com.educationapp.server.files.models.persistence.SubjectDB;
import com.educationapp.server.files.repositories.AccessToFileRepository;
import com.educationapp.server.files.repositories.FileRepository;
import com.educationapp.server.users.model.persistence.UserDB;
import com.educationapp.server.users.repositories.StudentRepository;
import com.educationapp.server.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class FileService {

    private final Path fileStorageLocation;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AccessToFileRepository accessToFileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    SubjectService subjectService;

    @Autowired
    public FileService() {
        fileStorageLocation = Paths.get("D:\\Programming\\Projects\\EducationAppServer")
                                   .toAbsolutePath()
                                   .normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
                                           ex);
        }
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

        final String fileLocation = String.valueOf(subjectDb.getId()) + "\\" + file.getFileTypeId() + "\\";

        try {
            if (fileLocation.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileLocation);
            }

            Path directory = this.fileStorageLocation.resolve(fileLocation);
            if (Files.notExists(directory)) {
                Files.createDirectories(directory);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileLocation.concat(fileName));
            Files.copy(file.getFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            fileRepository.save(new FileDB(fileLocation,
                                           fileName,
                                           subjectDb.getId(),
                                           file.getFileTypeId()));
            return fileLocation;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileLocation + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(final FileDB file) throws FileNotFoundException {
        try {
            Path filePath = fileStorageLocation.resolve(file.getPath() + file.getName()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException(String.format("File with id %s not found", file.getId()));
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException(String.format("File with id %s not found", file.getId()));
        }
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
            return fileRepository.findBySubjectId(subjectId)
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
                                 .map(fileRepository::findBySubjectId)
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
}
