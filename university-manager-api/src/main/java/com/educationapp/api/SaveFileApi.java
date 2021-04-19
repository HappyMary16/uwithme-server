package com.educationapp.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class SaveFileApi {

    @NotNull
    private String subjectName;

    @NotNull
    private Integer fileTypeId;

    @NotNull
    private MultipartFile file;
}
