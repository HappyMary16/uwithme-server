package com.mborodin.uwm.services;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.Optional;

import com.mborodin.uwm.api.exceptions.UnknownException;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.model.mapper.GroupMapper;
import com.mborodin.uwm.model.persistence.StudyGroupDataDb;
import com.mborodin.uwm.repositories.StudyGroupDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GroupService {

    private final StudyGroupDataRepository studyGroupDataRepository;
    private final ScheduleService scheduleService;
    private final GroupMapper groupMapper;

    public Optional<GroupApi> getById(final long groupId) {
        return studyGroupDataRepository.findById(groupId)
                                       .map(groupMapper::toGroupApi);
    }

    public GroupApi saveGroup(final GroupApi group) {
        final GroupApi createdGroup = Optional.of(group)
                                              .map(groupMapper::toStudyGroupDataDb)
                                              .map(studyGroupDataRepository::save)
                                              .map(groupMapper::toGroupApi)
                                              .orElseThrow(() -> new UnknownException(getLanguages()));

        log.info("A new group was created: {}", createdGroup);
        return createdGroup;
    }

    public void deleteAllByDepartmentId(final String departmentId) {
        studyGroupDataRepository.findAllByDepartmentId(departmentId)
                                .stream()
                                .map(StudyGroupDataDb::getId)
                                .forEach(scheduleService::deleteLessonsByGroupId);
        studyGroupDataRepository.deleteAllByDepartmentId(departmentId);
    }
}
