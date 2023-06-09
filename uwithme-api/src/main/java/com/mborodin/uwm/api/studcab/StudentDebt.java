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
    private int course;
    @JsonAlias({"prepod", "prepod_fio"})
    private String teacher;
    @JsonAlias("semestr")
    private int semester;
    @JsonAlias("subj_id")
    private String subjectId;
    @JsonAlias("subj_name")
    private String subject;
}
