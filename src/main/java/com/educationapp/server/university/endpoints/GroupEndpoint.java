package com.educationapp.server.university.endpoints;

import com.educationapp.server.common.api.admin.AddGroupApi;
import com.educationapp.server.university.models.Department;
import com.educationapp.server.university.models.Institute;
import com.educationapp.server.university.models.StudyGroup;
import com.educationapp.server.university.repositories.DepartmentRepository;
import com.educationapp.server.university.repositories.InstituteRepository;
import com.educationapp.server.university.repositories.StudyGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
@CrossOrigin("*")
public class GroupEndpoint {

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @PostMapping("/add")
    public StudyGroup addGroup(@RequestBody AddGroupApi addGroupApi) {
        final Long universityId = addGroupApi.getUniversityId();
        final String instituteName = addGroupApi.getInstituteName();
        final String departmentName = addGroupApi.getDepartmentName();

        final Long instituteId = instituteRepository.findByUniversityIdAndName(universityId, instituteName)
                                                    .orElseGet(() -> instituteRepository
                                                            .save(Institute.builder()
                                                                           .universityId(universityId)
                                                                           .name(instituteName)
                                                                           .build()))
                                                    .getId();
        final Long departmentId = departmentRepository.findByInstituteIdAndName(instituteId, departmentName)
                                                      .orElseGet(() -> departmentRepository
                                                              .save(Department.builder()
                                                                              .instituteId(instituteId)
                                                                              .name(departmentName)
                                                                              .build()))
                                                      .getId();
        final StudyGroup studyGroup = StudyGroup.builder()
                                                .name(addGroupApi.getGroupName())
                                                .course(addGroupApi.getCourse())
                                                .departmentId(departmentId)
                                                .build();

        return studyGroupRepository.save(studyGroup);
    }

}
