package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.Optional;

import com.educationapp.server.models.persistence.UniversityDb;
import com.educationapp.server.repositories.UniversityRepository;
import com.educationapp.server.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/university")
public class UniversityEndpoint {

    private final UniversityRepository universityRepository;

    @GetMapping
    public ResponseEntity<UniversityDb> getUniversity() {
        final Long universityId = UserContextHolder.getUniversityId();

        return Optional.ofNullable(universityId)
                       .flatMap(universityRepository::findById)
                       .map(university -> new ResponseEntity<>(university, OK))
                       .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}
