package com.mborodin.uwm.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mborodin.uwm.AbstractBaseTest;
import com.mborodin.uwm.api.structure.GroupApi;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GroupTests extends AbstractBaseTest {

    @Disabled
    @Test
    void groupCreation() {
        final GroupApi addGroupApi = GroupApi.builder()
                                             .departmentId(1)
                                             .name("TEST")
                                             .course(1)
                                             .visible(false)
                                             .build();

        final Long groupId = uwmClient.createGroup(addGroupApi);
        final GroupApi createdGroup = uwmClient.getGroupById(groupId);

        final GroupApi expectedGroup = GroupApi.builder()
                                               .id(groupId)
                                               .name("TEST")
                                               .visible(false)
                                               .course(1)
                                               .build();

        assertNotNull(groupId);
        assertEquals(createdGroup, expectedGroup);
    }
}
