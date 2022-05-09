package com.mborodin.uwm.services;

import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.model.mapper.DepartmentMapper;
import com.mborodin.uwm.repositories.DepartmentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentApi getById(final long departmentId) {
        return departmentRepository.findById(departmentId)
                                   .map(departmentMapper::toDepartmentApi)
                                   .orElse(null);
    }
}
