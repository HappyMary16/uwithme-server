package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.Optional;

import com.mborodin.uwm.api.exceptions.EntityNotFoundException;
import com.mborodin.uwm.api.structure.UniversityApi;
import com.mborodin.uwm.model.mapper.UniversityMapper;
import com.mborodin.uwm.repositories.UniversityRepository;
import com.mborodin.uwm.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/universities")
public class UniversityEndpoint {

    private final UniversityRepository universityRepository;
    private final UniversityMapper universityMapper;

    @GetMapping
    public UniversityApi getUniversity() {
        final Long universityId = UserContextHolder.getUniversityId();

        return Optional.of(universityId)
                       .flatMap(universityRepository::findById)
                       .map(universityMapper::toUniversityApi)
                       .orElseThrow(() -> new EntityNotFoundException(getLanguages()));
    }
}
