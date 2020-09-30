package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.educationapp.server.models.api.AccessToFileApi;
import com.educationapp.server.models.api.FileApi;
import com.educationapp.server.models.api.SaveFileApi;
import com.educationapp.server.models.api.UploadFileResponseApi;
import com.educationapp.server.models.persistence.FileDB;
import com.educationapp.server.repositories.FileRepository;
import com.educationapp.server.security.UserContextHolder;
import com.educationapp.server.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class FileEndpoint {

    private final FileService fileService;

    private final FileRepository fileRepository;

    public FileEndpoint(final FileService fileService,
                        final FileRepository fileRepository) {
        this.fileService = fileService;
        this.fileRepository = fileRepository;
    }

    @PostMapping("/uploadFile")
    public UploadFileResponseApi uploadFile(@RequestBody SaveFileApi saveFileApi) {
        final String fileName = fileService.saveFile(saveFileApi);
        final String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                                                  .path("/downloadFile/")
                                                                  .path(fileName)
                                                                  .toUriString();

        return new UploadFileResponseApi(fileName,
                                         fileDownloadUri,
                                         saveFileApi.getFile().getContentType(),
                                         saveFileApi.getFile().getSize());
    }

    @PostMapping("/uploadMultipleFiles/{username}/{subjectName}/{fileType:[1-9]}")
    public List<UploadFileResponseApi> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,
                                                           @PathVariable("username") final String username,
                                                           @PathVariable("subjectName") final String subjectName,
                                                           @PathVariable("fileType") final Long fileType) {
        return Arrays.stream(files)
                     .map(file -> uploadFile(new SaveFileApi(username,
                                                             subjectName,
                                                             fileType,
                                                             file)))
                     .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileId:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
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

    /**
     * For teachers
     *
     * @param subjectId
     * @return
     */
    @GetMapping("/files/{subjectId:[1-9]+}")
    public ResponseEntity<List<FileDB>> getFiles(@PathVariable("subjectId") final Long subjectId) {
        return new ResponseEntity<>(fileRepository.findAllBySubjectId(subjectId), OK);
    }

    /**
     * For students
     *
     * @param subjectId
     * @param username
     * @return
     */
    @GetMapping("/files/{username:.+}/{subjectId:[1-9]+}")
    public ResponseEntity<List<FileApi>> getFiles(@PathVariable("subjectId") final Long subjectId,
                                                  @PathVariable("username") final String username) {
        return new ResponseEntity<>(fileService.findByUsernameAndSubjectName(username, subjectId), OK);
    }

    @GetMapping("/files/{username:.+}")
    public ResponseEntity<List<FileApi>> getFiles(@PathVariable("username") final String username) {
        return new ResponseEntity<>(fileService.findByUsername(username), OK);
    }

    @PostMapping("/addAccess")
    public ResponseEntity addAccessToFiles(@RequestBody final AccessToFileApi accessToFileApi) {
        fileService.saveAccessToFile(accessToFileApi);
        return new ResponseEntity<>(OK);
    }

    @PostMapping("/updateAvatar/{userId:.+}")
    public ResponseEntity uploadAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile avatar) {
        fileService.updateAvatar(userId, avatar);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/avatar/{userId:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable("userId") final Long userId) {
        final Resource resource = fileService.loadAvatar(userId);

        if (resource == null) {
            //TODO change to not found or something else
            return new ResponseEntity<>(OK);
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

    @GetMapping("/avatar")
    public ResponseEntity<Resource> getAvatar() {
        return getAvatar(UserContextHolder.getUser().getId());
    }
}