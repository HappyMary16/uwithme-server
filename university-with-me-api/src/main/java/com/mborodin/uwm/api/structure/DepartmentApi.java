package com.mborodin.uwm.api.structure;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentApi {

    private long id;
    @JsonAlias("departmentName")
    private String name;
    private String shortName;
    private long instituteId;
    private long universityId;
}
