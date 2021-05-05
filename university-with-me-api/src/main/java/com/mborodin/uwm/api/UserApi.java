package com.mborodin.uwm.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public class UserApi {

    private String id;

    private String firstName;

    private String lastName;

    private String surname;

    private String username;

    private String phone;

    private String email;

    private Integer role;

    private String studyGroupName;

    private Long studyGroupId;

    private String instituteName;

    private String departmentName;

    private Long universityId;

    @Builder.Default
    private Boolean isAdmin = false;
}
