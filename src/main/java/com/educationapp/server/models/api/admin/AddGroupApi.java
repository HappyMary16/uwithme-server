package com.educationapp.server.models.api.admin;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddGroupApi {

    @NotNull
    private final Long universityId;

    @NotNull
    private final String instituteName;

    @NotNull
    private final String departmentName;

    @NotNull
    private final String groupName;

    @NotNull
    private final Integer course;

    @NotNull
    private final boolean isShowingInRegistration;
}