package com.educationapp.server.users.model;

import java.util.Collections;

import javax.validation.constraints.NotEmpty;

import com.educationapp.server.common.enums.Role;
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
    public Student(final Long id,
                   final String firstName,
                   final String lastName,
                   final String surname,
                   @NonNull @NotEmpty final String username,
                   @NonNull @NotEmpty final String password,
                   final String phone,
                   @NonNull @NotEmpty final String email,
                   final Boolean isAdmin,
                   final Long universityId,
                   final String studyGroupId,
                   final String studentId) {
        super(id,
              firstName,
              lastName,
              surname,
              username,
              password,
              phone,
              email,
              Role.STUDENT,
              isAdmin,
              universityId,
              Collections.singletonList(Role.STUDENT.name()));

        this.studyGroupId = studyGroupId;
        this.studentId = studentId;
    }
}
