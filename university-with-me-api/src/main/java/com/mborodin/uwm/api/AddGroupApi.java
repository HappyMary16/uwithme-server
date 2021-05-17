package com.mborodin.uwm.api;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class AddGroupApi {

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