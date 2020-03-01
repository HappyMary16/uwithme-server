package com.educationapp.server.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserApi {

    private String firstName;

    private String lastName;

    private String surname;

    private String username;

    private String password;

    private String phone;

    private String email;

    private Integer role;

    private String studentId;

    private String studyGroupName;

    private String instituteName;

    private String departmentName;

    private String scienceDegreeName;

    private String token;

    private Long universityId;

    @Builder.Default
    private Boolean isAdmin = false;
}
