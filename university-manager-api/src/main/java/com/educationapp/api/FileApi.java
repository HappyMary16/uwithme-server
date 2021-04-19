package com.educationapp.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@Getter
public class FileApi {

    @NotNull
    private Long fileId;

    @NotNull
    private String fileName;

    @NotNull
    private Integer type;

    @NotNull
    private Long subjectId;

    @NotNull
    private Date timeStartAccess;
}
