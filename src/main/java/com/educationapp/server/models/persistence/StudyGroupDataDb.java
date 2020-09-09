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
@SecondaryTables({
        @SecondaryTable(name = "departments",
                foreignKey = @ForeignKey(name = "department_id")),
        @SecondaryTable(name = "institutes",
                foreignKey = @ForeignKey(name = "institute_id"))
})
public class StudyGroupDataDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "institute_id")
    private Long instituteId;

    @Column(table = "departments", name = "name")
    private String departmentName;

    @Column(table = "institutes", name = "name")
    private String instituteName;

    @Column(table = "institutes", name = "university_id")
    private Long universityId;
}
