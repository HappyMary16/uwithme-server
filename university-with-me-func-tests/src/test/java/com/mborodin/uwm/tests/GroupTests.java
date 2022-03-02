package com.mborodin.uwm.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import javax.inject.Inject;

import com.mborodin.uwm.AbstractBaseTest;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.clients.GroupServiceClient;
import org.testng.annotations.Test;

public class GroupTests extends AbstractBaseTest {

    @Inject
    private GroupServiceClient groupServiceClient;

    @Test
    void groupCreation() {
        final GroupApi addGroupApi = GroupApi.builder()
                                             .departmentId(1)
                                             .name("TEST")
                                             .course(1)
                                             .visible(false)
                                             .build();

        final Long groupId = groupServiceClient.createGroup(addGroupApi);
        final GroupApi createdGroup = groupServiceClient.getGroupById(groupId);

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
