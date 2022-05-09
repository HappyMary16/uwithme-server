package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.exceptions.EntityNotFoundException;
import com.mborodin.uwm.api.exceptions.UnknownException;
import com.mborodin.uwm.api.structure.InstituteApi;
import com.mborodin.uwm.model.mapper.InstituteMapper;
import com.mborodin.uwm.model.persistence.InstituteDb;
import com.mborodin.uwm.repositories.InstituteRepository;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.InstituteService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/institutes")
public class InstituteEndpoint {

    private final InstituteRepository instituteRepository;
    private final InstituteService instituteService;
    private final InstituteMapper instituteMapper;

    @Secured("ROLE_ADMIN")
    @PostMapping
    public InstituteApi createInstitute(@RequestBody final InstituteApi institute) {
        log.info("Start institute creation. Institute: {}", institute);

        final long universityId = UserContextHolder.getUniversityId();
        final InstituteDb toCreate = InstituteDb.builder()
                                                .name(institute.getName())
                                                .universityId(universityId)
                                                .build();

        final InstituteApi createdInstitute = Optional.of(instituteRepository.save(toCreate))
                                                      .map(instituteMapper::toInstituteApi)
                                                      .orElseThrow(() -> new UnknownException(getLanguages()));

        log.info("Finish institute creation. Institute: {}", createdInstitute);
        return createdInstitute;
    }

    @GetMapping
    public List<InstituteApi> getInstitutesByUniversityId() {
        final Long universityId = UserContextHolder.getUniversityId();
        return instituteRepository.findAllByUniversityId(universityId)
                                  .stream()
                                  .map(instituteMapper::toInstituteApi)
                                  .collect(Collectors.toList());
    }

    @Secured({"ROLE_STUDENT", "ROLE_TEACHER"})
    @GetMapping("/user")
    public InstituteApi getInstitute() {
        final InstituteDb institute = instituteService.getInstituteForUser();

        return Optional.ofNullable(institute)
                       .map(instituteMapper::toInstituteApi)
                       .orElseThrow(() -> new EntityNotFoundException(getLanguages()));
    }
}
