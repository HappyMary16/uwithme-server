package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.educationapp.server.models.api.admin.AddGroupApi;
import com.educationapp.server.models.persistence.DepartmentDb;
import com.educationapp.server.models.persistence.InstituteDb;
import com.educationapp.server.models.persistence.StudyGroupDataDb;
import com.educationapp.server.repositories.DepartmentRepository;
import com.educationapp.server.repositories.InstituteRepository;
import com.educationapp.server.repositories.StudyGroupDataRepository;
import com.educationapp.server.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupEndpoint {

    private final InstituteRepository instituteRepository;

    private final DepartmentRepository departmentRepository;

    private final StudyGroupDataRepository studyGroupDataRepository;

    @PostMapping
    public ResponseEntity<?> addGroup(@RequestBody final AddGroupApi addGroupApi) {
        final Long universityId = UserContextHolder.getUser().getUniversityId();
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

        return new ResponseEntity<>(studyGroupDataRepository.save(group), OK);
    }

    @GetMapping(value = "/universityId/{universityId}")
    public ResponseEntity<?> getStudyGroupsByUniversityId(@PathVariable("universityId") final Long universityId) {
        return new ResponseEntity<>(studyGroupDataRepository.findAllByUniversityId(universityId), OK);
    }

    @GetMapping(value = "/{groupId}")
    public ResponseEntity<?> getStudyGroupById(@PathVariable("groupId") final Long groupId) {
        final StudyGroupDataDb group = studyGroupDataRepository.findById(groupId).orElse(null);

        if (group != null) {
            return new ResponseEntity<>(studyGroupDataRepository.findById(groupId), OK);
        } else {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    /**
     * Only for teachers
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getGroups() {
        final Long userId = UserContextHolder.getUser().getId();

        //add groups by curator
        return new ResponseEntity<>(studyGroupDataRepository.findAllByTeacher(userId), OK);
    }
}
