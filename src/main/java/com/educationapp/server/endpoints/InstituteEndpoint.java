package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.OK;

import com.educationapp.server.models.api.admin.AddInstituteApi;
import com.educationapp.server.models.persistence.InstituteDb;
import com.educationapp.server.repositories.InstituteRepository;
import com.educationapp.server.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/institutes")
public class InstituteEndpoint {

    private final InstituteRepository instituteRepository;

    @PostMapping
    public ResponseEntity<?> addInstitute(@RequestBody final AddInstituteApi addInstituteApi) {
        final Long universityId = UserContextHolder.getUser().getUniversityId();
        final InstituteDb institute = InstituteDb.builder()
                                                 .name(addInstituteApi.getInstituteName())
                                                 .universityId(universityId)
                                                 .build();
        return new ResponseEntity<>(instituteRepository.save(institute), OK);
    }

    @GetMapping
    public ResponseEntity<?> getInstitutesByUniversityId() {
        final Long universityId = UserContextHolder.getUser().getUniversityId();
        return new ResponseEntity<>(instituteRepository.findAllByUniversityId(universityId), OK);
    }
}
