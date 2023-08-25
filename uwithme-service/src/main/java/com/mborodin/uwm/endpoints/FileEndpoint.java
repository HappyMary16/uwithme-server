package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getId;
import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.AccessToFileApi;
import com.mborodin.uwm.api.FileApi;
import com.mborodin.uwm.api.UploadFileResponseApi;
import com.mborodin.uwm.api.exceptions.UnknownException;
import com.mborodin.uwm.model.persistence.FileDB;
import com.mborodin.uwm.repositories.FileRepository;
import com.mborodin.uwm.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/api/files")
public class FileEndpoint {

    private final FileService fileService;

    private final FileRepository fileRepository;

    @Secured("ROLE_TEACHER")
    @PostMapping("/{subjectId}/{fileType:[1-2]}")
    public List<UploadFileResponseApi> uploadMultipleFiles(@RequestParam("files") final MultipartFile[] files,
                                                           @PathVariable("subjectId") final Long subjectId,
                                                           @PathVariable("fileType") final Integer fileType) {

        return Arrays.stream(files)
                     .map(file -> {
                         final String fileName = fileService.saveFile(file, subjectId, fileType);
                         return new UploadFileResponseApi(fileName, file.getContentType(), file.getSize());
                     })
                     .collect(Collectors.toList());
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
                             .header("Content-Disposition", "attachment; filename=\"" + fileDB.getName() + "\"")
                             .body(resource);
    }

    @Secured("ROLE_TEACHER")
    @DeleteMapping("/{fileId}")
    public void deleteFile(@PathVariable final Long fileId) {
        final FileApi file = fileService.findFileById(fileId);

        if (!Objects.equals(file.getTeacherId(), getId())) {
            throw new UnknownException(getLanguages());
        }

        fileService.deleteFile(file);
    }

    @Secured("ROLE_SERVICE")
    @GetMapping("/{fileId}/info")
    public FileApi getFileInfo(@PathVariable final Long fileId) {
        return fileService.findFileById(fileId);
    }

    @Secured({"ROLE_STUDENT", "ROLE_TEACHER"})
    @GetMapping
    public List<FileApi> getFiles() {
        return fileService.findAllFiles();
    }

    @Secured("ROLE_SERVICE")
    @GetMapping("/groupId/{groupId}")
    public List<FileApi> getFilesByGroupId(@PathVariable final Long groupId) {
        return fileService.findFilesByGroupId(groupId);
    }

    @Secured("ROLE_TEACHER")
    @PostMapping("/access")
    public void addAccessToFiles(@RequestBody final AccessToFileApi accessToFileApi) {
        fileService.addAccessToFile(accessToFileApi);
    }

    @Secured({"ROLE_TEACHER", "ROLE_STUDENT"})
    @PostMapping("/avatar")
    public void uploadAvatar(@RequestParam("file") final MultipartFile avatar) {
        fileService.updateAvatar(avatar);
    }

    @Secured({"ROLE_TEACHER", "ROLE_STUDENT", "ROLE_ADMIN"})
    @GetMapping("/avatar/{userId:.+}")
    public ResponseEntity<Resource> getAvatar(@PathVariable("userId") final String userId) {
        final Resource resource = fileService.loadAvatar(userId);

        if (resource == null) {
            return ResponseEntity.notFound()
                                 .build();
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
