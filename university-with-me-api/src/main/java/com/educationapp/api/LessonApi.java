package com.educationapp.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class LessonApi {

    private final Long id;

    private final String subjectName;

    private final String teacherName;

    private final String lectureHall;

    private final String building;

    private final List<String> groups;

    private final Long weekDay;

    private final Long lessonTime;

    private final Long weekNumber;
}
