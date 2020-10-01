package com.educationapp.server.models.api;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class SaveFileApi {

    @NotNull
    private String subjectName;

    @NotNull
    private Long fileTypeId;

    @NotNull
    private MultipartFile file;
}
