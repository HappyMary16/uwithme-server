package com.educationapp.server.models.persistence;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule")
public class ScheduleDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "study_group_id")
    private Long studyGroupId;

    @Column(name = "lesson_number")
    private Long lessonNumber;

    @Column(name = "day_of_week")
    private Long dayOfWeek;

    @Column(name = "week_number")
    private Long weekNumber;

    @Column(name = "auditory")
    private String auditory;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "teacher_name")
    private String teacherName;
}