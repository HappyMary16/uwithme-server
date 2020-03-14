package com.educationapp.server.schedule.models.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateLessonApi {

    private final Long subjectId;

    private final String subjectName;

    private final Long teacherId;

    private final String teacherName;

    private final String lectureHall;

    private final List<Long> groups;

    private final Long weekDay;

    private final Long lessonTime;

    private final Long weekNumber;
}


