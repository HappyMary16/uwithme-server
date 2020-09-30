package com.educationapp.server.endpoints;

import java.util.List;

import com.educationapp.server.models.api.admin.AddGroupApi;
import com.educationapp.server.models.persistence.DepartmentDb;
import com.educationapp.server.models.persistence.InstituteDb;
import com.educationapp.server.models.persistence.StudyGroupDataDb;
import com.educationapp.server.repositories.DepartmentRepository;
import com.educationapp.server.repositories.InstituteRepository;
import com.educationapp.server.repositories.StudyGroupDataRepository;
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
    private final StudyGroupDataRepository studyGroupDataRepository;

    @PostMapping("/add")
    public ResponseEntity<StudyGroupDataDb> addGroup(@RequestBody final AddGroupApi addGroupApi) {
        final Long universityId = addGroupApi.getUniversityId();
        final String instituteName = addGroupApi.getInstituteName();
        final String departmentName = addGroupApi.getDepartmentName();

        final InstituteDb institute = instituteRepository.findByUniversityIdAndName(universityId, instituteName)
                                                         .orElseGet(() -> InstituteDb.builder()
                                                                                     .name(instituteName)
                                                                                     .universityId(universityId)
                                                                                     .build());
        final DepartmentDb department = departmentRepository.findByInstituteIdAndName(institute.getId(), departmentName)
                                                            .orElseGet(() -> DepartmentDb.builder()
                                                                                         .institute(institute)
                                                                                         .name(departmentName)
                                                                                         .build());
        final StudyGroupDataDb group = StudyGroupDataDb.builder()
                                                       .name(addGroupApi.getGroupName())
                                                       .course(addGroupApi.getCourse())
                                                       .department(department)
                                                       .isShowingInRegistration(addGroupApi.isShowingInRegistration())
                                                       .build();

        return new ResponseEntity<>(studyGroupDataRepository.save(group), HttpStatus.OK);
    }

    @GetMapping(value = "/{universityId}/universityId")
    public ResponseEntity<?> getStudyGroupsByUniversityId(@PathVariable("universityId") final Long universityId) {
        return new ResponseEntity<>(studyGroupDataRepository.findAllByUniversityId(universityId), HttpStatus.OK);
    }

    @GetMapping(value = "/{teacherId}/teacherId")
    public List<StudyGroupDataDb> getStudyGroupsByTeacherId(@PathVariable("teacherId") final Long teacherId) {
        return studyGroupDataRepository.findAllByTeacherId(teacherId);
    }

    @GetMapping(value = "/{groupId}/groupId")
    public ResponseEntity<?> getStudyGroupById(@PathVariable("groupId") final Long groupId) {
        final StudyGroupDataDb group = studyGroupDataRepository.findById(groupId).orElse(null);

        if (group != null) {
            return new ResponseEntity<>(studyGroupDataRepository.findById(groupId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
