package com.mborodin.uwm.endpoints;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.Optional;

import com.mborodin.uwm.api.AddGroupApi;
import com.mborodin.uwm.models.persistence.DepartmentDb;
import com.mborodin.uwm.models.persistence.InstituteDb;
import com.mborodin.uwm.models.persistence.StudyGroupDataDb;
import com.mborodin.uwm.repositories.DepartmentRepository;
import com.mborodin.uwm.repositories.InstituteRepository;
import com.mborodin.uwm.repositories.StudyGroupDataRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupEndpoint {

    private final InstituteRepository instituteRepository;

    private final DepartmentRepository departmentRepository;

    private final StudyGroupDataRepository studyGroupDataRepository;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public Long addGroup(@RequestBody final AddGroupApi addGroupApi) {
        final Long universityId = UserContextHolder.getUniversityId();
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
                                                       .isVisible(addGroupApi.isShowingInRegistration())
                                                       .build();

        return studyGroupDataRepository.save(group).getId();
    }

    @GetMapping(value = "/universityId/{universityId}")
    public ResponseEntity<?> getStudyGroupsByUniversityId(@PathVariable("universityId") final Long universityId) {
        return new ResponseEntity<>(studyGroupDataRepository.findAllByUniversityId(universityId), OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_SERVICE')")
    @GetMapping(value = "/{groupId}")
    public ResponseEntity<?> getStudyGroupById(@PathVariable("groupId") final Long groupId) {
        return studyGroupDataRepository.findById(groupId)
                .map(group -> new ResponseEntity<>(group, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    /**
     * Only for teachers
     *
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    @GetMapping
    public ResponseEntity<?> getGroups() {
        final String userId = UserContextHolder.getId();

        //add groups by curator
        return new ResponseEntity<>(studyGroupDataRepository.findAllByTeacher(userId), OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_STUDENT')")
    @GetMapping("/user")
    public ResponseEntity<StudyGroupDataDb> getGroup() {
        final Long groupId = UserContextHolder.getGroupId();

        return Optional.ofNullable(groupId)
                .flatMap(studyGroupDataRepository::findById)
                .map(group -> new ResponseEntity<>(group, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
