package com.mborodin.uwm.services;

import java.util.Optional;

import com.mborodin.uwm.models.persistence.DepartmentDb;
import com.mborodin.uwm.models.persistence.InstituteDb;
import com.mborodin.uwm.repositories.DepartmentRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class InstituteService {

    private final DepartmentRepository departmentRepository;

    public InstituteDb getInstituteForUser() {
        final var departmentId = UserContextHolder.getUserDb().getDepartmentId();

        return Optional.ofNullable(departmentId)
                       .flatMap(departmentRepository::findById)
                       .map(DepartmentDb::getInstitute)
                       .orElse(null);
    }

}
