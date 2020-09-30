package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.educationapp.server.models.api.AccessToFileApi;
import com.educationapp.server.models.api.SaveFileApi;
import com.educationapp.server.models.api.UploadFileResponseApi;
import com.educationapp.server.models.persistence.FileDB;
import com.educationapp.server.repositories.FileRepository;
import com.educationapp.server.security.UserContextHolder;
import com.educationapp.server.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/api/files")
public class FileEndpoint {

    private final FileService fileService;

    private final FileRepository fileRepository;

    @PostMapping("/{subjectName}/{fileType:[1-2]}")
    public ResponseEntity<?> uploadMultipleFiles(@RequestParam("files") final MultipartFile[] files,
                                                 @PathVariable("subjectName") final String subjectName,
                                                 @PathVariable("fileType") final Long fileType) {
        final List<UploadFileResponseApi> uploadedFiles = Arrays
                .stream(files)
                .map(file -> {
                    final SaveFileApi saveFileApi = new SaveFileApi(subjectName, fileType, file);
                    final String fileName = fileService.saveFile(saveFileApi);

                    return new UploadFileResponseApi(fileName,
                                                     saveFileApi.getFile().getContentType(),
                                                     saveFileApi.getFile().getSize());
                })
                .collect(Collectors.toList());

        return new ResponseEntity<>(uploadedFiles, OK);
    }

    @GetMapping("/{fileId:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable final Long fileId) {
        final FileDB fileDB = fileRepository.findById(fileId).orElse(new FileDB());
        final Resource resource = fileService.loadFile(fileDB);

        String contentType;
        try {
            contentType = Files.probeContentType(Path.of(fileDB.getName()));
        } catch (IOException ex) {
            //TODO add log
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(contentType))
                             .body(resource);
    }

    @GetMapping
    public ResponseEntity<?> getFiles() {
        return new ResponseEntity<>(fileService.findAllFiles(), OK);
    }

    @PostMapping("/access")
    public ResponseEntity<?> addAccessToFiles(@RequestBody final AccessToFileApi accessToFileApi) {
        fileService.addAccessToFile(accessToFileApi);
        return new ResponseEntity<>(OK);
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") final MultipartFile avatar) {
        fileService.updateAvatar(avatar);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/avatar")
    public ResponseEntity<?> getAvatar() {
        return getAvatar(UserContextHolder.getUser().getId());
    }

    @GetMapping("/avatar/{userId:.+}")
    public ResponseEntity<?> getAvatar(@PathVariable("userId") final Long userId) {
        final Resource resource = fileService.loadAvatar(userId);

        if (resource == null) {
            return new ResponseEntity<>(NOT_FOUND);
        } else {
            String contentType;
            try {
                contentType = Files.probeContentType(Path.of(resource.getFilename()));
            } catch (IOException ex) {
                //TODO add log
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                                 .contentType(MediaType.parseMediaType(contentType))
                                 .body(resource);
        }
    }
}