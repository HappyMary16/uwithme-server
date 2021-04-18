package com.educationapp.server.models.api.admin;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddUniversityApi {

    @NotNull
    private final String universityName;

    @NotNull
    @Size(min = 3, max = 20)
    private final String username;

    @NotNull
    @Size(min = 6, max = 50)
    private final String password;
}
