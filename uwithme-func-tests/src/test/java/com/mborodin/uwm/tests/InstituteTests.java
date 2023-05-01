package com.mborodin.uwm.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.mborodin.uwm.AbstractBaseTest;
import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.api.structure.UniversityApi;
import com.mborodin.uwm.client.client.core.InfoClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InstituteTests extends AbstractBaseTest {

    @Autowired
    private InfoClient infoClient;

    @Test
    void getInstitutesByTenantId() {
        final UniversityApi tenant = tenantClient.getUniversity();
        final List<DepartmentApi> institute = infoClient.getInstitutes(tenant.getId());

        assertTrue(institute.isEmpty());
    }

    @Test
    void createInstitute() {
        final UniversityApi tenant = tenantClient.getUniversity();
        final DepartmentApi institute = DepartmentApi.builder()
                                                     .name("TEST")
                                                     .universityId(tenant.getId())
                                                     .build();

        final DepartmentApi created = uwmClient.createDepartment(institute);
        final DepartmentApi expected = institute.toBuilder()
                                                .id(created.getId())
                                                .build();
        assertNotNull(created.getId());
        assertEquals(expected, created);

        final List<DepartmentApi> byTenantId = infoClient.getInstitutes(tenant.getId());
        assertEquals(1, byTenantId.size());
        assertEquals(created.getId(), byTenantId.get(0).getId());

        final List<DepartmentApi> byUsersTenantId = uwmClient.getDepartmentsFromUsersTenant();
        assertEquals(1, byUsersTenantId.size());
        assertEquals(created.getId(), byUsersTenantId.get(0).getId());

        uwmClient.deleteDepartment(created.getId());
        assertTrue(infoClient.getInstitutes(tenant.getId()).isEmpty());
        assertTrue(uwmClient.getDepartmentsFromUsersTenant().isEmpty());
    }
}
