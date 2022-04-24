package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.Optional;

import com.mborodin.uwm.api.exceptions.UnknownException;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.model.mapper.GroupMapper;
import com.mborodin.uwm.model.persistence.StudyGroupDataDb;
import com.mborodin.uwm.repositories.StudyGroupDataRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/groups")
public class GroupEndpoint {

    private final StudyGroupDataRepository studyGroupDataRepository;
    private final GroupMapper groupMapper;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public GroupApi addGroup(@RequestBody final GroupApi group) {
        log.info("Start group creation. Group: {}", group);

        final GroupApi createdGroup = Optional.of(group)
                                              .map(groupMapper::toStudyGroupDataDb)
                                              .map(studyGroupDataRepository::save)
                                              .map(groupMapper::toGroupApi)
                                              .orElseThrow(() -> new UnknownException(getLanguages()));

        log.info("Finish group creation. Group: {}", createdGroup);
        return createdGroup;
    }

    @GetMapping(value = "/universityId/{universityId}")
    public ResponseEntity<?> getStudyGroupsByUniversityId(@PathVariable("universityId") final Long universityId) {
        return new ResponseEntity<>(studyGroupDataRepository.findAllByUniversityId(universityId), OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_TEACHER", "ROLE_SERVICE"})
    @GetMapping(value = "/{groupId}")
    public ResponseEntity<?> getStudyGroupById(@PathVariable("groupId") final Long groupId) {
        return studyGroupDataRepository.findById(groupId)
                                       .map(group -> new ResponseEntity<>(group, OK))
                                       .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @Secured("ROLE_TEACHER")
    @GetMapping
    public ResponseEntity<?> getGroups() {
        final String userId = UserContextHolder.getId();

        //get groups by curator
        return new ResponseEntity<>(studyGroupDataRepository.findAllByTeacher(userId), OK);
    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/user")
    public ResponseEntity<StudyGroupDataDb> getGroup() {
        final Long groupId = UserContextHolder.getGroupId();

        return Optional.ofNullable(groupId)
                       .flatMap(studyGroupDataRepository::findById)
                       .map(group -> new ResponseEntity<>(group, OK))
                       .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
