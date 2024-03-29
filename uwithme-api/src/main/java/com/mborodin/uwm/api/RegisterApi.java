package com.mborodin.uwm.api;

import com.mborodin.uwm.api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class RegisterApi {

    private Role role;

    private Long groupId;

    private String departmentId;

    private Long universityId;

    private String instituteId;

    private String universityName;
}
