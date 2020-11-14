package com.educationapp.server.models.api;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Value
@AllArgsConstructor
@Getter
public class AccessToFileApi {

    @NotNull
    List<Long> fileIds;

    @NotNull
    List<Long> groupIds;
}
