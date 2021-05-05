package com.mborodin.uwm.api;

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
    private Integer fileTypeId;

    @NotNull
    private MultipartFile file;
}
