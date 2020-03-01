package com.educationapp.server.common.api.admin;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddUniversityApi {

    @NotNull
    private String universityName;

    @NotNull
    @Size(min = 3, max = 20)
    private String username;

    @NotNull
    @Size(min = 6, max = 50)
    private String password;

    @NotNull
    @Size(min = 6, max = 50)
    private String confirmPassword;
}
