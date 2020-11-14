package com.educationapp.server.models.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class RegisterApi {

    @NotNull
    @Size(max = 3)
    private Integer role;

    private Long groupId;

    private Long departmentId;

    private Long universityId;

    private Long instituteId;

    private String universityName;
}
