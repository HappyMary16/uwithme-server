package com.mborodin.uwm.api.studcab;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Information about a subject from a plan of education
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanSubject {

    @JsonAlias("audit")
    private String lectureHall;
    @JsonAlias("control")
    private String control;
    private String credit;
    @JsonAlias("indzav")
    private String individualTask;
    @JsonAlias("kabr")
    private String departmentShort;
    @JsonAlias("kafedra")
    private String department;
    @JsonAlias("kurs")
    private int course;
    @JsonAlias("semestr")
    private int semester;
    @JsonAlias("subj_id")
    private String subjectId;
    private String subject;
}
