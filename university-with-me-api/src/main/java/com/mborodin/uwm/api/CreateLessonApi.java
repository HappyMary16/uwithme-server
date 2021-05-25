package com.mborodin.uwm.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class CreateLessonApi {

    private final Long subjectId;

    private final String subjectName;

    private final String teacherId;

    private final String teacherName;

    private final Long lectureHall;

    private final List<Long> groups;

    private final List<Long> weekDays;

    private final List<Long> lessonTimes;

    private final List<Long> weekNumbers;
}


