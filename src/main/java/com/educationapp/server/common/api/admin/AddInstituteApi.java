package com.educationapp.server.common.api.admin;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddInstituteApi {

    @NotNull
    private Long universityId;

    @NotNull
    private String instituteName;
}
