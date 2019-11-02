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
public class Teacher extends User {

    private String departmentId;

    @Builder(toBuilder = true)
    public Teacher(final Long id, final String firstName, final String lastName,
                   @NonNull @NotEmpty final String nickname,
                   @NonNull @NotEmpty final String password,
                   @NonNull @NotEmpty final String passwordConfirm, final String phone,
                   @NonNull @NotEmpty final String email, final String departmentId) {
        super(id, firstName, lastName, nickname, password, passwordConfirm, phone, email);
        this.departmentId = departmentId;
    }
}
