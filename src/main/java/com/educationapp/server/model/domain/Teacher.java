package com.educationapp.server.model.domain;

import javax.validation.constraints.NotEmpty;

import com.educationapp.server.model.Role;
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
                   @NonNull @NotEmpty final String username,
                   @NonNull @NotEmpty final String password,
                   @NonNull @NotEmpty final String passwordConfirm, final String phone,
                   @NonNull @NotEmpty final String email, final String departmentId) {
        super(id, firstName, lastName, username, password, passwordConfirm, phone, email, Role.UNIVERSITY_ADMIN.name(), null);
        this.departmentId = departmentId;
    }
}
