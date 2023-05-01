package com.mborodin.uwm.api;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.api.structure.GroupApi;
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
    private String middleName;
    private String surname;
    private String phone;
    private String email;
    @Deprecated
    private Role role;
    @Deprecated
    private String studyGroupName;
    @Deprecated
    private Long studyGroupId;
    @Deprecated
    private String instituteName;
    @Deprecated
    private String departmentName;
    private Set<Role> roles;
    private GroupApi group;
    private DepartmentApi institute;
    private DepartmentApi department;
    private Long universityId;
    @Deprecated
    @Builder.Default
    private Boolean isAdmin = false;
}
