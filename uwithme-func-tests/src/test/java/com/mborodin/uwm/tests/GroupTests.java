package com.mborodin.uwm.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.mborodin.uwm.AbstractBaseTest;
import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.api.structure.InstituteApi;
import com.mborodin.uwm.api.structure.UniversityApi;
import com.mborodin.uwm.client.client.core.GroupClient;
import com.mborodin.uwm.client.client.core.InfoClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupTests extends AbstractBaseTest {

    @Autowired
    private GroupClient groupClient;
    @Autowired
    private InfoClient infoClient;

    @AfterEach
    public void cleanUp() {
        uwmClient.getInstitutesFromUsersTenant()
                 .forEach(institute -> uwmClient.deleteInstitute(institute.getId()));
    }

    @Test
    void getGroupsByDepartmentId() {
        final UniversityApi tenant = tenantClient.getUniversity();
        final InstituteApi institute = uwmClient.createInstitute(InstituteApi.builder()
                                                                             .name("TEST")
                                                                             .universityId(tenant.getId())
                                                                             .build());

        final DepartmentApi department = uwmClient.createDepartment(DepartmentApi.builder()
                                                                                 .name("TEST")
                                                                                 .universityId(tenant.getId())
                                                                                 .instituteId(institute.getId())
                                                                                 .build());

        final List<GroupApi> groups = infoClient.getGroupsAvailableForRegistration(department.getId());

        assertTrue(groups.isEmpty());
    }

    @Test
    void createGroup() {
        final UniversityApi tenant = tenantClient.getUniversity();
        final InstituteApi institute = uwmClient.createInstitute(InstituteApi.builder()
                                                                             .name("TEST")
                                                                             .universityId(tenant.getId())
                                                                             .build());

        final DepartmentApi department = uwmClient.createDepartment(DepartmentApi.builder()
                                                                                 .name("TEST")
                                                                                 .universityId(tenant.getId())
                                                                                 .instituteId(institute.getId())
                                                                                 .build());

        final GroupApi group = GroupApi.builder()
                                       .universityId(tenant.getId())
                                       .departmentId(department.getId())
                                       .name("TEST")
                                       .course(2)
                                       .visible(true)
                                       .build();
        final GroupApi created = groupClient.createGroup(group);
        final GroupApi expected = group.toBuilder()
                                       .id(created.getId())
                                       .build();
        assertNotNull(created.getId());
        assertEquals(expected, created);
        assertEquals(expected, groupClient.getGroupById(created.getId()));

        final List<GroupApi> byDepartmentId = infoClient.getGroupsAvailableForRegistration(department.getId());
        assertEquals(1, byDepartmentId.size());
        assertEquals(expected, byDepartmentId.get(0));

        final List<GroupApi> byTenantId = groupClient.getGroupsByTenantId(tenant.getId());
        assertEquals(1, byTenantId.size());
        assertEquals(expected, byTenantId.get(0));

        groupClient.deleteGroup(created.getId());
        assertTrue(infoClient.getGroupsAvailableForRegistration(department.getId()).isEmpty());
        assertTrue(groupClient.getGroupsByTenantId(tenant.getId()).isEmpty());
    }
}
