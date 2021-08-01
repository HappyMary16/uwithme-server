package com.mborodin.uwm.api.studcab;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectScore {

    private String control;
    private String credit;
    private String data;
    private String if_hvost;
    private String indzav;
    private String kabr;
    private String kafedra;
    private String oc_bol;
    private String oc_ets;
    private String oc_naz;
    private String oc_short;
    private String prepod;
    private String subj_id;
    private String subject;
}
