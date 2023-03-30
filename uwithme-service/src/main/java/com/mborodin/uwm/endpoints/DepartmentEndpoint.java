package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.QueryParam;

import com.mborodin.uwm.api.exceptions.EntityNotFoundException;
import com.mborodin.uwm.api.exceptions.UnknownException;
import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.model.mapper.DepartmentMapper;
import com.mborodin.uwm.repositories.TenantDepartmentRepository;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.DepartmentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentEndpoint {

    private final DepartmentService departmentService;
    private final DepartmentMapper departmentMapper;
    private final TenantDepartmentRepository tenantDepartmentRepository;

    @Secured("ROLE_ADMIN")
    @GetMapping("/{departmentId}")
    public DepartmentApi getDepartment(@PathVariable("departmentId") final String departmentId) {
        final var department =
                tenantDepartmentRepository.findById(departmentId)
                                          .orElseThrow(() -> new EntityNotFoundException(getLanguages()));

        return departmentMapper.toDepartmentApi(department);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{departmentId}/sub-departments")
    public List<DepartmentApi> getSubDepartments(@PathVariable("departmentId") final String departmentId) {
        return tenantDepartmentRepository.findAllByMainDepartmentId(departmentId)
                                         .stream()
                                         .map(departmentMapper::toDepartmentApi)
                                         .collect(Collectors.toList());
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public DepartmentApi createDepartment(@RequestBody final DepartmentApi department) {
        log.info("Start department creation. Department: {}", department);

        final var createdDepartment = Optional.of(department)
                                              .map(departmentMapper::toDb)
                                              .map(tenantDepartmentRepository::save)
                                              .map(departmentMapper::toDepartmentApi)
                                              .orElseThrow(() -> new UnknownException(getLanguages()));

        log.info("Finish department creation. Department: {}", createdDepartment);
        return createdDepartment;
    }

    @GetMapping
    public List<DepartmentApi> getDepartmentsByUniversityId(@QueryParam("universityId") final Long universityId) {
        return tenantDepartmentRepository.findAllByTenantIdAndMainDepartmentIdIsNull(universityId)
                                         .stream()
                                         .map(departmentMapper::toDepartmentApi)
                                         .collect(Collectors.toList());
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{departmentId}")
    public void deleteDepartment(@PathVariable("departmentId") final String departmentId) {
        departmentService.deleteDepartment(departmentId);
    }

    @Secured({"ROLE_STUDENT", "ROLE_TEACHER"})
    @GetMapping("/user")
    public DepartmentApi getDepartment() {
        final var departmentId = UserContextHolder.getUserDb().getDepartmentId();

        return Optional.ofNullable(departmentId)
                       .flatMap(tenantDepartmentRepository::findById)
                       .map(departmentMapper::toDepartmentApi)
                       .orElseThrow(() -> new EntityNotFoundException(getLanguages()));
    }
}
