package com.mborodin.uwm.api.studcab;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentScore {

    @JsonAlias("fio")
    private String fullName;
    private String group;
    @JsonAlias("n")
    private int place;
    @JsonAlias("sbal5")
    private String scoreNationalShort;
    @JsonAlias("sbal100")
    private String scoreBologna;
    @JsonAlias("studid")
    private String studentId;
}
