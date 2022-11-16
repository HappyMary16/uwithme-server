package com.mborodin.uwm.services;

import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.model.mapper.GroupMapper;
import com.mborodin.uwm.repositories.StudyGroupDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GroupService {

    private final StudyGroupDataRepository studyGroupDataRepository;
    private final GroupMapper groupMapper;

    public GroupApi getById(final long departmentId) {
        log.debug("getGroupById {}",departmentId);
        return studyGroupDataRepository.findById(departmentId)
                                       .map(groupMapper::toGroupApi)
                                       .orElse(null);
    }
}
