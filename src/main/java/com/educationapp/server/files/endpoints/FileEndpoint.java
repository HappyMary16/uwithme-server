package com.educationapp.server.files.endpoints;

import static org.springframework.http.HttpStatus.OK;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.educationapp.server.files.models.AccessToFileApi;
import com.educationapp.server.files.models.FileApi;
import com.educationapp.server.files.models.SaveFileApi;
import com.educationapp.server.files.models.UploadFileResponse;
import com.educationapp.server.files.models.persistence.File;
import com.educationapp.server.files.models.persistence.Subject;
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

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws
            FileNotFoundException {

        Resource resource = fileService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

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
    public ResponseEntity<List<File>> getFiles(@PathVariable("subjectId") final Long subjectId) {
        return new ResponseEntity<>(fileRepository.findBySubjectId(subjectId), OK);
    }

    @GetMapping("/subjects/{teacherUsername:.+}")
    public ResponseEntity<List<Subject>> getSubjects(@PathVariable("teacherUsername") final String teacherUsername) {
        return new ResponseEntity<>(subjectService.findSubjectNamesByTeacherUsername(teacherUsername), OK);
    }

    @PostMapping("/addAccess")
    public ResponseEntity addAccessToFiles(@RequestBody final AccessToFileApi accessToFileApi) {
        fileService.saveAccessToFile(accessToFileApi);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/files/{username:.+}/{subjectName:[1-9]+}")
    public ResponseEntity<List<FileApi>> getFiles(@PathVariable("subjectName") final String subjectName,
                                                  @PathVariable("username") final String username) {
        return new ResponseEntity<>(fileService.findByUsernameAndSubjectName(username, subjectName), OK);
    }

    @PostMapping("/subject/{username:.+}/{subjectName:.+}")
    public ResponseEntity<List<FileApi>> saveSubject(@PathVariable("subjectName") final String subjectName,
                                                     @PathVariable("username") final String username) {
        subjectService.save(username, subjectName);
        return new ResponseEntity<>(OK);
    }
}