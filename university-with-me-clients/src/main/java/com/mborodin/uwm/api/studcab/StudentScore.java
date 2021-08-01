package com.mborodin.uwm.api.studcab;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentScore {

    private String fio;
    private String group;
    private String n;
    private String rating;
    private String sbal5;
    private String sbal100;
    private String studid;
}
