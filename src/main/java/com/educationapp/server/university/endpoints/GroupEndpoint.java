package com.educationapp.server.university.endpoints;

import java.util.List;

import com.educationapp.server.common.api.admin.AddGroupApi;
import com.educationapp.server.university.models.Department;
import com.educationapp.server.university.models.Institute;
import com.educationapp.server.university.models.StudyGroup;
import com.educationapp.server.university.repositories.DepartmentRepository;
import com.educationapp.server.university.repositories.InstituteRepository;
import com.educationapp.server.university.repositories.StudyGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupEndpoint {

    private final InstituteRepository instituteRepository;
    private final DepartmentRepository departmentRepository;
    private final StudyGroupRepository studyGroupRepository;

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

    @RequestMapping(value = "/{universityId}/universityId", method = RequestMethod.GET)
    public List<StudyGroup> getStudyGroupsByUniversityId(@PathVariable("universityId") final Long universityId) {
        return studyGroupRepository.findAllByUniversityId(universityId);
    }

    @RequestMapping(value = "/{teacherId}/teacherId", method = RequestMethod.GET)
    public List<StudyGroup> getStudyGroupsByTeacherId(@PathVariable("teacherId") final Long teacherId) {
        return studyGroupRepository.findAllByTeacherId(teacherId);
    }
}
