package com.mborodin.uwm.api.studcab;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDebt {

    @JsonAlias("control")
    private String control;
    private String credit;
    private String data;
    @JsonAlias("indzav")
    private String individualTask;
    @JsonAlias("kabr")
    private String departmentShort;
    @JsonAlias("kafedra")
    private String department;
    @JsonAlias("kurs")
    private String course;
    @JsonAlias("prepod")
    private String teacher;
    @JsonAlias("semestr")
    private String semester;
    @JsonAlias("subj_id")
    private String subjectId;
    private String subject;
}
