package com.educationapp.server.models.api.admin;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor
@Getter
@Value
public class AddStudentsToGroupApi {

    @NotNull
    List<String> studentsIds;

    @NotNull
    Long groupId;
}
