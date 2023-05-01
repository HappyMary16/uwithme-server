package com.mborodin.uwm.client.client;

import java.util.List;

import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.UserApi;
import com.mborodin.uwm.api.structure.DepartmentApi;
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

    @PostMapping("/api/departments")
    DepartmentApi createDepartment(DepartmentApi institute);

    @GetMapping("/api/departments")
    List<DepartmentApi> getDepartmentsFromUsersTenant();

    @GetMapping("/api/departments/{departmentId}/sub-departments")
    List<DepartmentApi> getSubDepartments(@PathVariable final String departmentId);

    @DeleteMapping("/api/departments/{departmentId}")
    void deleteDepartment(@PathVariable String departmentId);
}
