package com.mborodin.uwm.client.client.core;

import java.util.List;

import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.api.structure.UniversityApi;
import com.mborodin.uwm.client.config.AuthClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "uwm-service",
        url = "#{'${uwm.api.host:localhost}:${uwm.api.port}'}",
        configuration = {AuthClientConfiguration.class})
public interface InfoClient {

    @GetMapping("/api/info/universities")
    List<UniversityApi> getUniversities();

    @GetMapping("/api/info/institutes/{universityId}")
    List<DepartmentApi> getInstitutes(@PathVariable Long universityId);

    @GetMapping("/api/info/departments/{instituteId}")
    List<DepartmentApi> getDepartments(@PathVariable String instituteId);

    @GetMapping("/api/info/groups/{departmentId}")
    List<GroupApi> getGroupsAvailableForRegistration(@PathVariable String departmentId);
}
