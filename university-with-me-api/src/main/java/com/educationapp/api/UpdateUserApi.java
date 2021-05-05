package com.educationapp.api;

import lombok.Value;

@Value
public class UpdateUserApi {

    String firstName;
    String lastName;
    String surname;
    String email;
    Long studyGroupId;
    Long instituteId;
    Long departmentId;
    Long universityId;
}
