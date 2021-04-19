package com.educationapp.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
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


