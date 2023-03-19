package com.mborodin.uwm.services;

import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.model.mapper.DepartmentMapper;
import com.mborodin.uwm.model.persistence.TenantDepartmentDb;
import com.mborodin.uwm.repositories.StudyGroupDataRepository;
import com.mborodin.uwm.repositories.TenantDepartmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class DepartmentService {

    private final StudyGroupDataRepository studyGroupDataRepository;
    private final TenantDepartmentRepository tenantDepartmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentApi getById(final String departmentId) {
        return tenantDepartmentRepository.findById(departmentId)
                                         .map(departmentMapper::toDepartmentApi)
                                         .orElse(null);
    }

    @Transactional
    public void deleteByInstituteId(final String instituteId) {
        tenantDepartmentRepository.findAllByMainDepartmentId(instituteId)
                                  .stream()
                                  .map(TenantDepartmentDb::getDepartmentId)
                                  .forEach(studyGroupDataRepository::deleteAllByDepartmentId);

         tenantDepartmentRepository.deleteAllByMainDepartmentId(instituteId);
    }

    @Transactional
    public void deleteDepartment(final String departmentId) {
        studyGroupDataRepository.deleteAllByDepartmentId(departmentId);
        tenantDepartmentRepository.deleteById(departmentId);
    }
}
