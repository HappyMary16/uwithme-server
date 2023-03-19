package com.mborodin.uwm.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.mborodin.uwm.AbstractBaseTest;
import com.mborodin.uwm.api.structure.InstituteApi;
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
        final List<InstituteApi> institute = infoClient.getInstitutes(tenant.getId());

        assertTrue(institute.isEmpty());
    }

    @Test
    void createInstitute() {
        final UniversityApi tenant = tenantClient.getUniversity();
        final InstituteApi institute = InstituteApi.builder()
                                                   .name("TEST")
                                                   .universityId(tenant.getId())
                                                   .build();

        final InstituteApi created = uwmClient.createInstitute(institute);
        final InstituteApi expected = institute.toBuilder()
                                               .id(created.getId())
                                               .build();
        assertNotNull(created.getId());
        assertEquals(expected, created);

        final List<InstituteApi> byTenantId = infoClient.getInstitutes(tenant.getId());
        assertEquals(1, byTenantId.size());
        assertEquals(created.getId(), byTenantId.get(0).getId());

        final List<InstituteApi> byUsersTenantId = uwmClient.getInstitutesFromUsersTenant();
        assertEquals(1, byUsersTenantId.size());
        assertEquals(created.getId(), byUsersTenantId.get(0).getId());

        uwmClient.deleteInstitute(created.getId());
        assertTrue(infoClient.getInstitutes(tenant.getId()).isEmpty());
        assertTrue(uwmClient.getInstitutesFromUsersTenant().isEmpty());
    }
}
