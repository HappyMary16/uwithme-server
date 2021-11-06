package com.mborodin.uwm.endpoints;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.AccessToFileApi;
import com.mborodin.uwm.api.FileApi;
import com.mborodin.uwm.api.SaveFileApi;
import com.mborodin.uwm.api.UploadFileResponseApi;
import com.mborodin.uwm.models.persistence.FileDB;
import com.mborodin.uwm.repositories.FileRepository;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/api/files")
public class FileEndpoint {

    private final FileService fileService;

    private final FileRepository fileRepository;

    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    @PostMapping("/{subjectName}/{fileType:[1-2]}")
    public List<UploadFileResponseApi> uploadMultipleFiles(@RequestParam("files") final MultipartFile[] files,
                                                           @PathVariable("subjectName") final String subjectName,
                                                           @PathVariable("fileType") final Integer fileType) {
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

        return uploadedFiles;
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable final Long fileId) {
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

    @GetMapping("/{fileId}/info")
    public FileApi getFileInfo(@PathVariable final Long fileId) {
        return fileService.findFileById(fileId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT', 'ROLE_TEACHER')")
    @GetMapping
    public List<FileApi> getFiles() {
        return fileService.findAllFiles();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SERVICE')")
    @GetMapping("/groupId/{groupId}")
    public List<FileApi> getFilesByGroupId(@PathVariable final Long groupId) {
        return fileService.findFilesByGroupId(groupId);
    }

    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    @PostMapping("/access")
    public void addAccessToFiles(@RequestBody final AccessToFileApi accessToFileApi) {
        fileService.addAccessToFile(accessToFileApi);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER', 'ROLE_STUDENT')")
    @PostMapping("/avatar")
    public void uploadAvatar(@RequestParam("file") final MultipartFile avatar) {
        fileService.updateAvatar(avatar);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER', 'ROLE_STUDENT')")
    @GetMapping("/avatar")
    public ResponseEntity<Resource> getAvatar() {
        return getAvatar(UserContextHolder.getId());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER', 'ROLE_STUDENT', 'ROLE_ADMIN')")
    @GetMapping("/avatar/{userId:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable("userId") final String userId) {
        final Resource resource = fileService.loadAvatar(userId);

        if (resource == null) {
            return null;
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
