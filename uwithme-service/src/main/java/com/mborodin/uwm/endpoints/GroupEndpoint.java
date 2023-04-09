package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.ws.rs.QueryParam;

import com.mborodin.uwm.api.exceptions.EntityNotFoundException;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.model.persistence.StudyGroupDataDb;
import com.mborodin.uwm.repositories.StudyGroupDataRepository;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.GroupService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupEndpoint {

    private final GroupService groupService;
    private final StudyGroupDataRepository studyGroupDataRepository;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public GroupApi addGroup(@RequestBody final GroupApi group) {
        log.info("Start a group creation. Group: {}", group);

        return groupService.saveGroup(group);
    }

    @GetMapping
    public List<StudyGroupDataDb> getStudyGroupsByUniversityId(@QueryParam("departmentId") String departmentId) {
        if (Objects.nonNull(departmentId)) {
            return studyGroupDataRepository.findAllByVisibleAndDepartmentId(true, departmentId);
        }

        final Long universityId = UserContextHolder.getUniversityId();
        return studyGroupDataRepository.findAllByUniversityId(universityId);
    }

    @GetMapping(value = "/{groupId}")
    public StudyGroupDataDb getStudyGroupById(@PathVariable("groupId") final Long groupId) {
        return studyGroupDataRepository.findById(groupId)
                                       .orElseThrow(() -> new EntityNotFoundException(getLanguages()));
    }

//    @Secured("ROLE_TEACHER")
//    @GetMapping
//    public List<StudyGroupDataDb> getGroups() {
//        final String userId = UserContextHolder.getId();
//
//        //get groups by curator
//        return studyGroupDataRepository.findAllByTeacher(userId);
//    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/user")
    public StudyGroupDataDb getGroup() {
        final Long groupId = UserContextHolder.getGroupId();

        return Optional.ofNullable(groupId)
                       .flatMap(studyGroupDataRepository::findById)
                       .orElseThrow(() -> new EntityNotFoundException(getLanguages()));
    }

    @Transactional
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable("groupId") final long groupId) {
        studyGroupDataRepository.deleteById(groupId);
    }
}
