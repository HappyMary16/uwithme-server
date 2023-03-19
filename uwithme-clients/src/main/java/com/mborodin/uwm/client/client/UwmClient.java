package com.mborodin.uwm.client.client;

import java.util.List;

import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.api.structure.InstituteApi;
import com.mborodin.uwm.client.config.AuthClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * TODO: add description.
 *
 * @author mariia.borodin (mborodin)
 * @since 1.1
 */
@FeignClient(name = "uwm-service",
        url = "#{'${uwm.api.host:localhost}:${uwm.api.port}'}",
        configuration = {AuthClientConfiguration.class})
public interface UwmClient {

    @PostMapping("/api/auth/signUp")
    UserApi register(RegisterApi group);

    @DeleteMapping("/api/users")
    void deleteAccount();

    @PostMapping("/api/institutes")
    InstituteApi createInstitute(InstituteApi institute);
    @GetMapping("api/institutes")
    List<InstituteApi> getInstitutesFromUsersTenant();
    @DeleteMapping("api/institutes/{instituteId}")
    void deleteInstitute(@PathVariable String instituteId);

    @PostMapping("/api/departments")
    DepartmentApi createDepartment(DepartmentApi institute);
    @GetMapping("api/departments")
    List<DepartmentApi> getDepartmentsFromUsersTenant();
    @DeleteMapping("api/departments/{departmentId}")
    void deleteDepartment(@PathVariable String departmentId);
}
