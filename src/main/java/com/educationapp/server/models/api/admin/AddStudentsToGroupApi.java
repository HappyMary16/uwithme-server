package com.educationapp.server.models.api.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddStudentsToGroupApi {

    @NotNull
    private List<String> studentsIds;

    @NotNull
    private Long groupId;
}
