package com.mborodin.uwm.services;

import java.util.Optional;

import com.mborodin.uwm.api.structure.InstituteApi;
import com.mborodin.uwm.model.mapper.InstituteMapper;
import com.mborodin.uwm.model.persistence.TenantDepartmentDb;
import com.mborodin.uwm.repositories.TenantDepartmentRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class InstituteService {

    private final DepartmentService departmentService;
    private final InstituteMapper instituteMapper;
    private final TenantDepartmentRepository tenantDepartmentRepository;

    public TenantDepartmentDb getInstituteForUser() {
        final var departmentId = UserContextHolder.getUserDb().getDepartmentId();

        return Optional.ofNullable(departmentId)
                       .flatMap(tenantDepartmentRepository::findById)
                       .map(TenantDepartmentDb::getMainDepartmentId)
                       .flatMap(tenantDepartmentRepository::findById)
                       .orElse(null);
    }

    public InstituteApi getById(final String instituteId) {
        return tenantDepartmentRepository.findById(instituteId)
                                         .map(instituteMapper::toInstituteApi)
                                         .orElse(null);
    }

    @Transactional
    public void deleteById(final String instituteId) {
        departmentService.deleteDepartment(instituteId);
    }
}