package com.educationapp.server.models.api;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FileApi {

    @NotNull
    private Long fileId;

    @NotNull
    private String fileName;

    @NotNull
    private Long type;

    @NotNull
    private Long subjectId;

    @NotNull
    private Date timeStartAccess;
}
