package com.educationapp.server.endpoints;

import com.educationapp.server.models.api.admin.AddDepartmentApi;
import com.educationapp.server.models.persistence.DepartmentDb;
import com.educationapp.server.models.persistence.InstituteDb;
import com.educationapp.server.repositories.DepartmentRepository;
import com.educationapp.server.repositories.InstituteRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/department")
public class DepartmentEndpoint {

    private final InstituteRepository instituteRepository;

    private final DepartmentRepository departmentRepository;

    public DepartmentEndpoint(
            final InstituteRepository instituteRepository, final DepartmentRepository departmentRepository) {
        this.instituteRepository = instituteRepository;
        this.departmentRepository = departmentRepository;
    }

    @PostMapping("/add")
    public DepartmentDb addDepartment(@RequestBody AddDepartmentApi addDepartmentApi) {
        final Long universityId = addDepartmentApi.getUniversityId();
        final String instituteName = addDepartmentApi.getInstituteName();

        final InstituteDb institute = instituteRepository.findByUniversityIdAndName(universityId, instituteName)
                                                         .orElseGet(() -> instituteRepository
                                                                 .save(InstituteDb.builder()
                                                                                  .universityId(universityId)
                                                                                  .name(instituteName)
                                                                                  .build()));
        final DepartmentDb department = DepartmentDb.builder()
                                                    .name(addDepartmentApi.getDepartmentName())
                                                    .instituteId(institute.getId())
                                                    .build();

        return departmentRepository.save(department);
    }
}
