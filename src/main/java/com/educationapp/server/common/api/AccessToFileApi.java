package com.educationapp.server.common.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccessToFileApi {

    @NotNull
    private List<Long> fileIds;

    @NotNull
    private Long groupId;
}
