package com.mborodin.uwm.api.studcab;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentInfo {

    @JsonAlias("fakultet")
    private String institute;
    @JsonAlias("fam")
    private String surname;
    @JsonAlias("fid")
    private String instituteId;
    @JsonAlias("gid")
    private String groupId;
    @JsonAlias("grupa")
    private String group;
    @JsonAlias("kafedra")
    private String department;
    @JsonAlias("kid")
    private String departmentId;
    @JsonAlias("kurs")
    private String course;
    @JsonAlias("imya")
    private String firstName;
    @JsonAlias("oplata")
    private String payment;
    @JsonAlias("osvitprog")
    private String educationProgram;
    @JsonAlias("otch")
    private String fatherName;
    @JsonAlias("speciality")
    private String speciality;
    @JsonAlias("specialization")
    private String specialization;
    @JsonAlias("st_cod")
    private String studentId;
    @JsonAlias("train_form")
    private String educationForm;
    @JsonAlias("train_level")
    private String degree;
}
