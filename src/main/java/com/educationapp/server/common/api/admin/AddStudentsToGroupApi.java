package com.educationapp.server.common.api.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddStudentsToGroupApi {

    @NotNull
    private List<Long> studentsIds;

    @NotNull
    private Long groupId;
}
