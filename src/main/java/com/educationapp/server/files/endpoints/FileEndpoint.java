package com.educationapp.server.files.endpoints;

import static org.springframework.http.HttpStatus.OK;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.educationapp.server.common.api.AccessToFileApi;
import com.educationapp.server.common.api.FileApi;
import com.educationapp.server.common.api.SaveFileApi;
import com.educationapp.server.files.models.UploadFileResponse;
import com.educationapp.server.files.models.persistence.FileDB;
import com.educationapp.server.files.models.persistence.SubjectDB;
import com.educationapp.server.files.repositories.FileRepository;
import com.educationapp.server.files.services.FileService;
import com.educationapp.server.files.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class FileEndpoint {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private SubjectService subjectService;

    private Path fileStorageLocation = Paths.get("D:\\Programming\\Projects\\EducationAppServer")
            .toAbsolutePath()
                                        .normalize();

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestBody SaveFileApi saveFileApi) {
        String fileName = fileService.saveFile(saveFileApi);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                            .path("/downloadFile/")
                                                            .path(fileName)
                                                            .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                                      saveFileApi.getFile().getContentType(), saveFileApi.getFile().getSize());
    }

    @PostMapping("/uploadMultipleFiles/{subjectId:[1-9]+}/{fileType:[1-9]}")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
                                                        @PathVariable("subjectId") final Long subjectId,
                                                        @PathVariable("fileType") final Long fileType) {
        return Arrays.stream(files)
                     .map(file -> uploadFile(new SaveFileApi(subjectId,
                                                             fileType,
                                                             file)))
                     .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileId:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId, HttpServletRequest request) throws
            FileNotFoundException {

        final FileDB fileDB = fileRepository.findById(fileId).orElse(new FileDB());
        Resource resource = fileService.loadFileAsResource(fileDB);

        String contentType;
        try {
            contentType = Files.probeContentType(
                    fileStorageLocation.resolve(fileDB.getPath() + fileDB.getName()).normalize());
        } catch (IOException ex) {

            contentType = "application/octet-stream";
            return ResponseEntity.ok()
                                 .contentType(MediaType.parseMediaType(contentType))
                                 .header(HttpHeaders.CONTENT_DISPOSITION,
                                         "attachment; filename=\"" + resource.getFilename() + "\"")
                                 .body(resource);
        }
        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(contentType))
                             .header(HttpHeaders.CONTENT_DISPOSITION,
                                     "attachment; filename=\"" + resource.getFilename() + "\"")
                             .body(resource);
    }

    @GetMapping("/files/{subjectId:[1-9]+}")
    public ResponseEntity<List<FileDB>> getFiles(@PathVariable("subjectId") final Long subjectId) {
        return new ResponseEntity<>(fileRepository.findBySubjectId(subjectId), OK);
    }

    @GetMapping("/subjects/{teacherUsername:.+}")
    public ResponseEntity<List<SubjectDB>> getSubjects(@PathVariable("teacherUsername") final String teacherUsername) {
        return new ResponseEntity<>(subjectService.findSubjectNamesByTeacherUsername(teacherUsername), OK);
    }

    @PostMapping("/addAccess")
    public ResponseEntity addAccessToFiles(@RequestBody final AccessToFileApi accessToFileApi) {
        fileService.saveAccessToFile(accessToFileApi);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/files/{username:.+}/{subjectId:[1-9]+}")
    public ResponseEntity<List<FileApi>> getFiles(@PathVariable("subjectId") final Long subjectId,
                                                  @PathVariable("username") final String username) {
        return new ResponseEntity<>(fileService.findByUsernameAndSubjectName(username, subjectId), OK);
    }

    @PostMapping("/subject/{username:.+}/{subjectName:.+}")
    public ResponseEntity<List<FileApi>> saveSubject(@PathVariable("subjectName") final String subjectName,
                                                     @PathVariable("username") final String username) {
        subjectService.save(username, subjectName);
        return new ResponseEntity<>(OK);
    }
}