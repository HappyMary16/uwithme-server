package com.mborodin.uwm.api.structure;

import com.fasterxml.jackson.annotation.JsonAlias;
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

    private long id;
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