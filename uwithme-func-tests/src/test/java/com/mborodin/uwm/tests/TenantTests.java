package com.mborodin.uwm.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.mborodin.uwm.AbstractBaseTest;
import com.mborodin.uwm.api.structure.UniversityApi;
import com.mborodin.uwm.client.client.core.InfoClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TenantTests extends AbstractBaseTest {

    @Autowired
    private InfoClient infoClient;

    @Test
    void getUsersTenant() {
        final UniversityApi tenant = tenantClient.getUniversity();

        assertNotNull(tenant);
        assertNotNull(tenant.getId());
        assertEquals(TENANT_NAME, tenant.getName());
    }

    @Test
    void getAllTenants() {
        final List<UniversityApi> tenants = infoClient.getUniversities();

        assertFalse(tenants.isEmpty());
        tenants.forEach(tenant -> assertNotNull(tenant.getId()));
        assertTrue(tenants.stream().map(UniversityApi::getName).anyMatch(TENANT_NAME::equals));
    }
}
