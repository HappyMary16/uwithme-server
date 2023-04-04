package com.mborodin.uwm.api;

import lombok.Value;

@Value
public class UwmUserApi {

    String userId;
    Long groupId;
    String departmentId;
    Long universityId;
}
