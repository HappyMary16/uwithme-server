package com.mborodin.uwm.api.studcab;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectScore {

    @JsonAlias("fio")
    private String control;
    private String credit;
    private String data;
    @JsonAlias("if_hvost")
    private String isDebt;
    @JsonAlias("indzav")
    private String individualTask;
    @JsonAlias("kabr")
    private String departmentShort;
    @JsonAlias("kafedra")
    private String department;
    @JsonAlias("oc_bol")
    private int scoreBologna;
    @JsonAlias("oc_ects")
    private String score_ECTS;
    @JsonAlias("oc_naz")
    private String scoreNational;
    @JsonAlias("oc_short")
    private int scoreNationalShort;
    @JsonAlias("prepod")
    private String teacher;
    @JsonAlias("subj_id")
    private String subjectId;
    private String subject;
}
