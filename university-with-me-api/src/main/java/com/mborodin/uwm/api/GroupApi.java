package com.mborodin.uwm.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupApi {

    private Long id;

    private String name;

    private Integer course;

    private Long teacherId;

    private Boolean isVisible;

//    private DepartmentApi department;
}