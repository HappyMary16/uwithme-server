package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.QueryParam;

import com.mborodin.uwm.api.exceptions.EntityNotFoundException;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.model.mapper.GroupMapper;
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
    private final GroupMapper groupMapper;
    private final StudyGroupDataRepository studyGroupDataRepository;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public GroupApi addGroup(@RequestBody final GroupApi group) {
        log.info("Start a group creation. Group: {}", group);

        return groupService.saveGroup(group);
    }

    @GetMapping
    public Collection<GroupApi> getStudyGroupsByUniversityId(@QueryParam("departmentId") String departmentId) {
        if (Objects.nonNull(departmentId)) {
            return studyGroupDataRepository.findAllByDepartmentId(departmentId)
                                           .stream()
                                           .map(groupMapper::toGroupApi)
                                           .collect(Collectors.toList());
        }

        final Long universityId = UserContextHolder.getUniversityId();
        return studyGroupDataRepository.findAllByUniversityId(universityId)
                                       .stream()
                                       .map(groupMapper::toGroupApi)
                                       .collect(Collectors.toList());
    }

    @GetMapping(value = "/{groupId}")
    public GroupApi getStudyGroupById(@PathVariable("groupId") final Long groupId) {
        return studyGroupDataRepository.findById(groupId)
                                       .map(groupMapper::toGroupApi)
                                       .orElseThrow(() -> new EntityNotFoundException(getLanguages()));
    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/user")
    public GroupApi getGroup() {
        final Long groupId = UserContextHolder.getGroupId();

        return Optional.ofNullable(groupId)
                       .flatMap(studyGroupDataRepository::findById)
                       .map(groupMapper::toGroupApi)
                       .orElseThrow(() -> new EntityNotFoundException(getLanguages()));
    }

    @Transactional
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable("groupId") final long groupId) {
        studyGroupDataRepository.deleteById(groupId);
    }
}
