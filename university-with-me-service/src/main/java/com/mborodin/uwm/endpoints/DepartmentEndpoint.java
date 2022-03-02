package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.exceptions.EntityNotFoundException;
import com.mborodin.uwm.api.exceptions.UnknownException;
import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.model.mapper.DepartmentMapper;
import com.mborodin.uwm.repositories.DepartmentRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentEndpoint {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public DepartmentApi createDepartment(@RequestBody final DepartmentApi department) {
        log.info("Start department creation. Department: {}", department);

        final var createdDepartment = Optional.of(department)
                                              .map(departmentMapper::toDepartmentDb)
                                              .map(departmentRepository::save)
                                              .map(departmentMapper::toDepartmentApi)
                                              .orElseThrow(() -> new UnknownException(getLanguages()));

        log.info("Finish department creation. Department: {}", createdDepartment);
        return createdDepartment;
    }

    @GetMapping
    public List<DepartmentApi> getDepartmentsByUsersUniversityId() {
        final Long universityId = UserContextHolder.getUniversityId();
        return departmentRepository.findAllByUniversityId(universityId)
                                   .stream()
                                   .map(departmentMapper::toDepartmentApi)
                                   .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT', 'ROLE_TEACHER')")
    @GetMapping("/user")
    public DepartmentApi getDepartment() {
        final var departmentId = UserContextHolder.getUserDb().getDepartmentId();

        return Optional.ofNullable(departmentId)
                       .flatMap(departmentRepository::findById)
                       .map(departmentMapper::toDepartmentApi)
                       .orElseThrow(() -> new EntityNotFoundException(getLanguages()));
    }
}
