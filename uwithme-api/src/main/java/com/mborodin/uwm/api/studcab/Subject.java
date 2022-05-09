package com.mborodin.uwm.api.studcab;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    @JsonAlias("Aud")
    private String lectureHall;
    @JsonAlias("Name")
    private String name;
    @JsonAlias("Prepod")
    private String teacher;
    @JsonAlias("vid")
    private String vid;
}
