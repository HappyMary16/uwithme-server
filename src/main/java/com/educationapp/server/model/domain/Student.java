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
public class Student extends User {

    private String studyGroupId;
    private String studentId;

    @Builder(toBuilder = true)
    public Student(final Long id, final String firstName, final String lastName,
                   @NonNull @NotEmpty final String nickname,
                   @NonNull @NotEmpty final String password,
                   @NonNull @NotEmpty final String passwordConfirm, final String phone,
                   @NonNull @NotEmpty final String email, final String studyGroupId,
                   final String studentId) {
        super(id, firstName, lastName, nickname, password, passwordConfirm, phone, email);
        this.studyGroupId = studyGroupId;
        this.studentId = studentId;
    }
}
