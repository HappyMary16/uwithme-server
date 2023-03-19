package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.exceptions.EntityNotFoundException;
import com.mborodin.uwm.api.exceptions.UnknownException;
import com.mborodin.uwm.api.structure.InstituteApi;
import com.mborodin.uwm.model.mapper.InstituteMapper;
import com.mborodin.uwm.model.persistence.TenantDepartmentDb;
import com.mborodin.uwm.repositories.TenantDepartmentRepository;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.InstituteService;
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
@RequestMapping("/api/institutes")
public class InstituteEndpoint {

    private final InstituteService instituteService;
    private final InstituteMapper instituteMapper;
    private final TenantDepartmentRepository tenantDepartmentRepository;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public InstituteApi createInstitute(@RequestBody final InstituteApi institute) {
        log.info("Start institute creation. Institute: {}", institute);

        final long universityId = UserContextHolder.getUniversityId();
        final var toCreate = TenantDepartmentDb.builder()
                                                .name(institute.getName())
                                                .tenantId(universityId)
                                                .build();

        final InstituteApi createdInstitute = Optional.of(tenantDepartmentRepository.save(toCreate))
                                                      .map(instituteMapper::toInstituteApi)
                                                      .orElseThrow(() -> new UnknownException(getLanguages()));

        log.info("Finish institute creation. Institute: {}", createdInstitute);
        return createdInstitute;
    }

    @GetMapping
    public List<InstituteApi> getInstitutesByUniversityId() {
        final Long universityId = UserContextHolder.getUniversityId();
        return tenantDepartmentRepository.findAllByTenantIdAndMainDepartmentIdIsNull(universityId)
                                         .stream()
                                         .map(instituteMapper::toInstituteApi)
                                         .collect(Collectors.toList());
    }

    @Secured({"ROLE_STUDENT", "ROLE_TEACHER"})
    @GetMapping("/user")
    public InstituteApi getInstitute() {
        final TenantDepartmentDb institute = instituteService.getInstituteForUser();

        return Optional.ofNullable(institute)
                       .map(instituteMapper::toInstituteApi)
                       .orElseThrow(() -> new EntityNotFoundException(getLanguages()));
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{instituteId}")
    public void deleteInstitute(@PathVariable("instituteId") final String instituteId) {
        instituteService.deleteById(instituteId);
    }
}
