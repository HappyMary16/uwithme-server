package com.mborodin.uwm.services;

import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.model.mapper.DepartmentMapper;
import com.mborodin.uwm.repositories.TenantDepartmentRepository;
import com.mborodin.uwm.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class DepartmentService {

    private final GroupService groupService;
    private final UserRepository userRepository;
    private final TenantDepartmentRepository tenantDepartmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentApi getById(final String departmentId) {
        return tenantDepartmentRepository.findById(departmentId)
                                         .map(departmentMapper::toDepartmentApi)
                                         .orElse(null);
    }

    @Transactional
    public void deleteDepartment(final String departmentId) {
        tenantDepartmentRepository.findAllByMainDepartmentId(departmentId)
                                  .forEach(department -> deleteDepartment(department.getDepartmentId()));

        userRepository.findAllByDepartmentId(departmentId)
                      .stream()
                      .peek(user -> user.setDepartmentId(null))
                      .peek(user -> user.setGroupId(null))
                      .forEach(userRepository::save);

        groupService.deleteAllByDepartmentId(departmentId);
        tenantDepartmentRepository.deleteById(departmentId);
    }
}
