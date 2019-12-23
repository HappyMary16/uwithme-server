package com.educationapp.server.files.endpoints;

import static org.springframework.http.HttpStatus.OK;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.educationapp.server.files.FileRepository;
import com.educationapp.server.files.models.SaveFileApi;
import com.educationapp.server.files.models.UploadFileResponse;
import com.educationapp.server.files.models.persistence.File;
import com.educationapp.server.files.services.FileStorageService;
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
public class SaveFileEndpoint {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileRepository fileRepository;

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestBody SaveFileApi file) {
        String fileName = fileStorageService.saveFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                            .path("/downloadFile/")
                                                            .path(fileName)
                                                            .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri,
                                      file.getFiles()[0].getContentType(), file.getFiles()[0].getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestBody SaveFileApi files) {
        return Arrays.stream(files.getFiles())
                     .map(file -> uploadFile(new SaveFileApi(files.getSubjectId(),
                                                             files.getFileTypeId(),
                                                             new MultipartFile[]{file})))
                     .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws
            FileNotFoundException {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            //logger.info("Could not determine file type.");        }

            // Fallback to the default content type if type could not be determined
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
    public ResponseEntity<List<File>> getAllFiles(@PathVariable("subjectId") final Long subjectId) {
        return new ResponseEntity<>(fileRepository.findBySubjectId(subjectId), OK);
    }
}