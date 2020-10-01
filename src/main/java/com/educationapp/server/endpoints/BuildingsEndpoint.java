package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.OK;

import com.educationapp.server.repositories.BuildingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/buildings")
public class BuildingsEndpoint {

    private final BuildingsRepository buildingsRepository;

    @GetMapping("/{universityId}")
    public ResponseEntity<?> getBuildings(@PathVariable("universityId") final Long universityId) {
        return new ResponseEntity<>(buildingsRepository.findAllByUniversityId(universityId), OK);
    }
}
