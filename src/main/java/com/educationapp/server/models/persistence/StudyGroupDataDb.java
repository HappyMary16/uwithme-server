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
public class StudyGroupDataDb {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "course")
    private Integer course;

    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "is_showing_in_registration")
    private Boolean isShowingInRegistration;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentDb department;
}