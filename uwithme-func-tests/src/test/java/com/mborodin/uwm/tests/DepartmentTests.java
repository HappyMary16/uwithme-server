package com.mborodin.uwm.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.mborodin.uwm.AbstractBaseTest;
import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.api.structure.InstituteApi;
import com.mborodin.uwm.api.structure.UniversityApi;
import com.mborodin.uwm.client.client.core.InfoClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DepartmentTests extends AbstractBaseTest {

    @Autowired
    private InfoClient infoClient;

    @AfterEach
    public void cleanUp() {
        uwmClient.getInstitutesFromUsersTenant()
                 .forEach(institute -> uwmClient.deleteInstitute(institute.getId().toString()));
    }

    @Test
    void getDepartmentsByInstituteId() {
        final UniversityApi tenant = tenantClient.getUniversity();
        final InstituteApi institute = uwmClient.createInstitute(InstituteApi.builder()
                                                                             .name("TEST")
                                                                             .universityId(tenant.getId())
                                                                             .build());
        final List<DepartmentApi> department = infoClient.getDepartments(institute.getId().toString());

        assertTrue(department.isEmpty());
    }

    @Test
    void createDepartment() {
        final UniversityApi tenant = tenantClient.getUniversity();
        final InstituteApi institute = uwmClient.createInstitute(InstituteApi.builder()
                                                                             .name("TEST")
                                                                             .universityId(tenant.getId())
                                                                             .build());

        final DepartmentApi department = DepartmentApi.builder()
                                                      .name("TEST")
                                                      .universityId(tenant.getId())
                                                      .instituteId(institute.getId())
                                                      .build();
        final DepartmentApi created = uwmClient.createDepartment(department);
        final DepartmentApi expected = department.toBuilder()
                                                 .id(created.getId())
                                                 .build();
        assertNotNull(created.getId());
        assertEquals(expected, created);

        final List<DepartmentApi> byInstituteId = infoClient.getDepartments(institute.getId().toString());
        assertEquals(1, byInstituteId.size());
        assertEquals(created.getId(), byInstituteId.get(0).getId());

        final List<DepartmentApi> byUsersTenantId = uwmClient.getDepartmentsFromUsersTenant();
        assertEquals(1, byUsersTenantId.size());
        assertEquals(created.getId(), byUsersTenantId.get(0).getId());

        uwmClient.deleteDepartment(created.getId().toString());
        assertTrue(infoClient.getDepartments(institute.getId().toString()).isEmpty());
        assertTrue(uwmClient.getDepartmentsFromUsersTenant().isEmpty());
    }
}
