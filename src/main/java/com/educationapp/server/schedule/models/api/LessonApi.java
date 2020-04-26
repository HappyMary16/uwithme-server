package com.educationapp.server.schedule.models.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class LessonApi {

    private final String subjectName;

    private final String teacherName;

    private final String lectureHall;

    private final String groupName;

    private final Long weekDay;

    private final Long lessonTime;

    private final Long weekNumber;
}
