package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.OK;

import com.educationapp.server.models.api.admin.AddInstituteApi;
import com.educationapp.server.models.persistence.InstituteDb;
import com.educationapp.server.repositories.InstituteRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/institutes")
public class InstituteEndpoint {

    private final InstituteRepository instituteRepository;

    @PostMapping
    public ResponseEntity<?> addInstitute(@RequestBody final AddInstituteApi addInstituteApi) {
        final InstituteDb institute = InstituteDb.builder()
                                                 .name(addInstituteApi.getInstituteName())
                                                 .universityId(addInstituteApi.getUniversityId())
                                                 .build();
        return new ResponseEntity<>(instituteRepository.save(institute), OK);
    }
}
