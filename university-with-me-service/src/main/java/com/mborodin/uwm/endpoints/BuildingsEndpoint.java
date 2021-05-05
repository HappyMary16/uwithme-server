package com.mborodin.uwm.endpoints;

import static org.springframework.http.HttpStatus.OK;

import com.mborodin.uwm.repositories.BuildingsRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/api/buildings")
public class BuildingsEndpoint {

    private final BuildingsRepository buildingsRepository;

    @GetMapping
    public ResponseEntity<?> getBuildings() {
        final Long universityId = UserContextHolder.getUniversityId();
        return new ResponseEntity<>(buildingsRepository.findAllByUniversityId(universityId), OK);
    }
}
