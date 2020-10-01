package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import com.educationapp.server.models.persistence.SubjectDB;
import com.educationapp.server.repositories.SubjectRepository;
import com.educationapp.server.services.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/subjects")
public class SubjectEndpoint {

    private final SubjectService subjectService;
    private final SubjectRepository subjectRepository;

    @GetMapping
    public ResponseEntity<List<SubjectDB>> getSubjects() {
        return new ResponseEntity<>(subjectService.findSubjectsByTeacherUsername(), OK);
    }

    @GetMapping("/{universityId:.+}")
    public ResponseEntity<List<SubjectDB>> getSubjectsByUniversityId(
            @PathVariable("universityId") final Long universityId) {
        return new ResponseEntity<>(subjectRepository.findAllByUniversityId(universityId), OK);
    }

    @PostMapping("/{username:.+}/{subjectName:.+}")
    public ResponseEntity<?> saveSubject(@PathVariable("subjectName") final String subjectName,
                                         @PathVariable("username") final String username) {
        subjectService.save(username, subjectName);
        return new ResponseEntity<>(OK);
    }
}
