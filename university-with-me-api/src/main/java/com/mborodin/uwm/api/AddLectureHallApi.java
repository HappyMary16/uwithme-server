package com.mborodin.uwm.api;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddLectureHallApi {

    @NotNull
    private final String buildingName;

    @NotNull
    private final String lectureHallName;

    @NotNull
    private final Integer placeNumber;
}
