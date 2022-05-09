package com.mborodin.uwm.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

@Value
@AllArgsConstructor
@Getter
public class AccessToFileApi {

    List<Long> fileIds;
    List<Long> groupIds;
}
