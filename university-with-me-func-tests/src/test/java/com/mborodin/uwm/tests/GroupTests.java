package com.mborodin.uwm.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import javax.inject.Inject;

import com.mborodin.uwm.AbstractBaseTest;
import com.mborodin.uwm.api.AddGroupApi;
import com.mborodin.uwm.api.GroupApi;
import com.mborodin.uwm.clients.GroupServiceClient;
import org.testng.annotations.Test;

public class GroupTests extends AbstractBaseTest {

    @Inject
    private GroupServiceClient groupServiceClient;

    @Test
    void groupCreation() {
        final AddGroupApi addGroupApi = AddGroupApi.builder()
                                                   .instituteName("TEST")
                                                   .departmentName("TEST")
                                                   .groupName("TEST")
                                                   .course(1)
                                                   .isShowingInRegistration(false)
                                                   .build();

        final Long groupId = groupServiceClient.createGroup(addGroupApi);
        final GroupApi createdGroup = groupServiceClient.getGroupById(groupId);

        final GroupApi expectedGroup = GroupApi.builder()
                                               .id(groupId)
                                               .name("TEST")
                                               .isVisible(false)
                                               .course(1)
                                               .build();

        assertNotNull(groupId);
        assertEquals(createdGroup, expectedGroup);
    }
}
