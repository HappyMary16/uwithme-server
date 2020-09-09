package com.educationapp.server.models.persistence;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "study_groups")
public class StudyGroupDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "institute_id")
    private Long instituteId;

    @Column(name = "course")
    private Integer course;

    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "is_showing_in_registration")
    private boolean isShowingInRegistration;
}
