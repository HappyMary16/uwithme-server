package com.educationapp.server.endpoints;

import java.util.List;

import com.educationapp.server.models.api.admin.AddGroupApi;
import com.educationapp.server.models.persistence.DepartmentDb;
import com.educationapp.server.models.persistence.InstituteDb;
import com.educationapp.server.models.persistence.StudyGroupDb;
import com.educationapp.server.repositories.DepartmentRepository;
import com.educationapp.server.repositories.InstituteRepository;
import com.educationapp.server.repositories.StudyGroupDataRepository;
import com.educationapp.server.repositories.StudyGroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupEndpoint {

    private final InstituteRepository instituteRepository;
    private final DepartmentRepository departmentRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final StudyGroupDataRepository studyGroupDataRepository;

    @PostMapping("/add")
    public StudyGroupDb addGroup(@RequestBody final AddGroupApi addGroupApi) {
        final Long universityId = addGroupApi.getUniversityId();
        final String instituteName = addGroupApi.getInstituteName();
        final String departmentName = addGroupApi.getDepartmentName();

        final Long instituteId = instituteRepository.findByUniversityIdAndName(universityId, instituteName)
                                                    .orElseGet(() -> instituteRepository
                                                            .save(InstituteDb.builder()
                                                                             .universityId(universityId)
                                                                             .name(instituteName)
                                                                             .build()))
                                                    .getId();
        final Long departmentId = departmentRepository.findByInstituteIdAndName(instituteId, departmentName)
                                                      .orElseGet(() -> departmentRepository
                                                              .save(DepartmentDb.builder()
                                                                                .instituteId(instituteId)
                                                                                .name(departmentName)
                                                                                .build()))
                                                      .getId();
        final StudyGroupDb studyGroup = StudyGroupDb.builder()
                                                    .name(addGroupApi.getGroupName())
                                                    .course(addGroupApi.getCourse())
                                                    .departmentId(departmentId)
                                                    .instituteId(instituteId)
                                                    .isShowingInRegistration(addGroupApi.isShowingInRegistration())
                                                    .build();

        return studyGroupRepository.save(studyGroup);
    }

    @GetMapping(value = "/{universityId}/universityId")
    public ResponseEntity<?> getStudyGroupsByUniversityId(@PathVariable("universityId") final Long universityId) {
        return new ResponseEntity<>(studyGroupDataRepository.findAllByUniversityId(universityId), HttpStatus.OK);
    }

    @GetMapping(value = "/{teacherId}/teacherId")
    public List<StudyGroupDb> getStudyGroupsByTeacherId(@PathVariable("teacherId") final Long teacherId) {
        return studyGroupRepository.findAllByTeacherId(teacherId);
    }

    @GetMapping(value = "/{groupId}/groupId")
    public ResponseEntity<?> getStudyGroupById(@PathVariable("groupId") final Long groupId) {
        return new ResponseEntity<>(studyGroupDataRepository.findById(groupId), HttpStatus.OK);
    }
}
