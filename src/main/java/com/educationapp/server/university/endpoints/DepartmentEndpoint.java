package com.educationapp.server.university.endpoints;

import com.educationapp.server.common.api.admin.AddDepartmentApi;
import com.educationapp.server.university.models.Department;
import com.educationapp.server.university.models.Institute;
import com.educationapp.server.university.repositories.DepartmentRepository;
import com.educationapp.server.university.repositories.InstituteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/department")
@CrossOrigin("*")
public class DepartmentEndpoint {

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping("/add")
    public Department addDepartment(@RequestBody AddDepartmentApi addDepartmentApi) {
        final Long universityId = addDepartmentApi.getUniversityId();
        final String instituteName = addDepartmentApi.getInstituteName();

        final Institute institute = instituteRepository.findByUniversityIdAndName(universityId, instituteName)
                                                       .orElseGet(() -> instituteRepository
                                                               .save(Institute.builder()
                                                                              .universityId(universityId)
                                                                              .name(instituteName)
                                                                              .build()));
        final Department department = Department.builder()
                                                .name(addDepartmentApi.getDepartmentName())
                                                .instituteId(institute.getId())
                                                .build();

        return departmentRepository.save(department);
    }
}
