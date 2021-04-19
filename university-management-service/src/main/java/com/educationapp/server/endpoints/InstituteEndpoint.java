package com.educationapp.server.endpoints;

import com.educationapp.api.AddInstituteApi;
import com.educationapp.server.models.persistence.InstituteDb;
import com.educationapp.server.repositories.InstituteRepository;
import com.educationapp.server.security.UserContextHolder;
import com.educationapp.server.services.InstituteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@AllArgsConstructor
@RestController
@RequestMapping("/api/institutes")
public class InstituteEndpoint {

    private final InstituteRepository instituteRepository;
    private final InstituteService instituteService;

    @PreAuthorize("hasAuthority('ADMIN')")
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

    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER')")
    @GetMapping("/user")
    public ResponseEntity<InstituteDb> getInstitute() {
        final InstituteDb institute = instituteService.getInstituteForUser();

        return Optional.ofNullable(institute)
                       .map(i -> new ResponseEntity<>(i, OK))
                       .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
