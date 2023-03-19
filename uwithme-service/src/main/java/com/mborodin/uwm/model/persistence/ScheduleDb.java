package com.mborodin.uwm.model.persistence;

import java.util.List;

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
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private SubjectDB subject;
    private Long lessonNumber;
    private Long dayOfWeek;
    private Long weekNumber;

    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "auditory", referencedColumnName = "id")
    private LectureHallDb auditory;
    private String subjectName;
    private String teacherName;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "schedule_group",
            joinColumns = {@JoinColumn(name = "schedule_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_id")}
    )
    private List<StudyGroupDb> groups;
}
