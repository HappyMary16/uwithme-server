package com.mborodin.uwm.api;

import lombok.Value;

@Value
public class UpdateUserApi {

    Long groupId;
    Long instituteId;
    String departmentId;
    Long universityId;
}
