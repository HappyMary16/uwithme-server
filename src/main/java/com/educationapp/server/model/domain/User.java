package com.educationapp.server.model.domain;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    protected Long id;
    protected String firstName;
    protected String lastName;
    @NonNull
    @NotEmpty
    protected String nickname;
    @NonNull
    @NotEmpty
    protected String password;
    @NonNull
    @NotEmpty
    protected String passwordConfirm;
    protected String phone;
    @NonNull
    @NotEmpty
    protected String email;
}
