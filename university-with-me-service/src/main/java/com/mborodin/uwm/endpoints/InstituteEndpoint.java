package com.mborodin.uwm.endpoints;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.Optional;

import com.mborodin.uwm.api.AddInstituteApi;
import com.mborodin.uwm.models.persistence.InstituteDb;
import com.mborodin.uwm.repositories.InstituteRepository;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.InstituteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/institutes")
public class InstituteEndpoint {

    private final InstituteRepository instituteRepository;
    private final InstituteService instituteService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> addInstitute(@RequestBody final AddInstituteApi addInstituteApi) {
        final Long universityId = UserContextHolder.getUniversityId();
        final InstituteDb institute = InstituteDb.builder()
                                                 .name(addInstituteApi.getInstituteName())
                                                 .universityId(universityId)
                                                 .build();
        return new ResponseEntity<>(instituteRepository.save(institute), OK);
    }

    @GetMapping
    public ResponseEntity<?> getInstitutesByUniversityId() {
        final Long universityId = UserContextHolder.getUniversityId();
        return new ResponseEntity<>(instituteRepository.findAllByUniversityId(universityId), OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STUDENT', 'ROLE_TEACHER')")
    @GetMapping("/user")
    public ResponseEntity<InstituteDb> getInstitute() {
        final InstituteDb institute = instituteService.getInstituteForUser();

        return Optional.ofNullable(institute)
                       .map(i -> new ResponseEntity<>(i, OK))
                       .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
