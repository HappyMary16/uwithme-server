package com.educationapp.server.files.services;

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

import com.educationapp.server.common.exception.FileStorageException;
import com.educationapp.server.common.api.AccessToFileApi;
import com.educationapp.server.common.api.FileApi;
import com.educationapp.server.files.models.persistence.AccessToFileDB;
import com.educationapp.server.files.models.persistence.SubjectDB;
import com.educationapp.server.files.repositories.AccessToFileRepository;
import com.educationapp.server.files.repositories.FileRepository;
import com.educationapp.server.common.api.SaveFileApi;
import com.educationapp.server.files.models.persistence.FileDB;
import com.educationapp.server.files.repositories.SubjectRepository;
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
    private SubjectRepository subjectService;

    @Autowired
    public FileService() {
        this.fileStorageLocation = Paths.get("D:\\Programming\\Projects\\EducationAppServer\\src")
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
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getFile().getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getFile().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


            fileRepository.save(new FileDB(targetLocation.toString(), fileName.split("\\.")[0], file.getSubjectId(), file.getFileTypeId()));
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("FileDB not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("FileDB not found " + fileName);
        }
    }

    public void saveAccessToFile(final AccessToFileApi accessToFileApi) {
        accessToFileApi.getFileIds()
                       .forEach(fileId -> accessToFileRepository.save(new AccessToFileDB(accessToFileApi.getGroupId(),
                                                                                         fileId,
                                                                                         new Date())));
    }

    public List<FileApi> findByUsernameAndSubjectName(final String username, final String subjectName) {
        final Long userId = userRepository.findByUsername(username).get().getId();
        final Long studyGroupId = studentRepository.findById(userId).get().getStudyGroupId();
        return accessToFileRepository.findAllByStudyGroupId(studyGroupId)
                                     .stream()
                                     .map(accessToFileDB -> {
                                         final FileDB fileDB = fileRepository.findById(accessToFileDB.getFileId()).get();
                                         final SubjectDB
                                                 subjectDB = subjectService.findById(fileDB.getSubjectId()).get();
                                         return new FileApi(accessToFileDB.getFileId(),
                                                            fileDB.getName(),
                                                            subjectDB.getName(),
                                                            accessToFileDB.getDateAddAccess());
                                     })
                                     .filter(file -> file.getSubjectName().equals(subjectName))
                                     .collect(Collectors.toList());
    }

}
