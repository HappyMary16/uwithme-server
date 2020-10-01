package com.educationapp.server.models.api.admin;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddInstituteApi {

    @NotNull
    private final String instituteName;
}
