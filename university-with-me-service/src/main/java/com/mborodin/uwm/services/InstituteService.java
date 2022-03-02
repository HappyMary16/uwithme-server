package com.mborodin.uwm.services;

import java.util.Optional;

import com.mborodin.uwm.api.structure.InstituteApi;
import com.mborodin.uwm.model.mapper.InstituteMapper;
import com.mborodin.uwm.model.persistence.DepartmentDb;
import com.mborodin.uwm.model.persistence.InstituteDb;
import com.mborodin.uwm.repositories.DepartmentRepository;
import com.mborodin.uwm.repositories.InstituteRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class InstituteService {

    private final DepartmentRepository departmentRepository;
    private final InstituteRepository instituteRepository;
    private final InstituteMapper instituteMapper;

    public InstituteDb getInstituteForUser() {
        final var departmentId = UserContextHolder.getUserDb().getDepartmentId();

        return Optional.ofNullable(departmentId)
                       .flatMap(departmentRepository::findById)
                       .map(DepartmentDb::getInstituteId)
                       .flatMap(instituteRepository::findById)
                       .orElse(null);
    }

    public InstituteApi getById(final long instituteId) {
        return instituteRepository.findById(instituteId)
                                  .map(instituteMapper::toInstituteApi)
                                  .orElse(null);
    }
}