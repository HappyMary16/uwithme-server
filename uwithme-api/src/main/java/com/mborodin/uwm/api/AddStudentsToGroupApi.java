package com.mborodin.uwm.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@AllArgsConstructor
@Getter
@Value
public class AddStudentsToGroupApi {

    List<String> studentsIds;
    Long groupId;
}
