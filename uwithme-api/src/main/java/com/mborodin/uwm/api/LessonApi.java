package com.mborodin.uwm.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LessonApi {

    private Long id;

    private String subjectName;

    private String teacherName;

    private String lectureHall;

    private String building;

    private List<String> groups;

    private Long weekDay;

    private Long lessonTime;

    private Long weekNumber;
}
