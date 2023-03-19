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
public class GroupApi {

    private Long id;
    @JsonAlias("groupName")
    private String name;
    private int educationStartYear;
    @Deprecated
    private int course;
    private Long teacherId;
    @JsonAlias("isShowingInRegistration")
    private boolean visible;
    private long departmentId;
    private long universityId;
}