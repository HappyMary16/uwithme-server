package com.mborodin.uwm.api;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddDepartmentApi {

    @NotNull
    private final String instituteName;

    @NotNull
    private final String departmentName;
}
