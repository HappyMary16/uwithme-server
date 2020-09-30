package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import com.educationapp.server.models.api.FileApi;
import com.educationapp.server.models.persistence.SubjectDB;
import com.educationapp.server.repositories.SubjectRepository;
import com.educationapp.server.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SubjectEndpoint {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping("/subjects/{username:.+}")
    public ResponseEntity<List<SubjectDB>> getSubjects(@PathVariable("username") final String username) {
        return new ResponseEntity<>(subjectService.findSubjectsByTeacherUsername(username), OK);
    }

    @GetMapping("/university/subjects/{universityId:.+}")
    public ResponseEntity<List<SubjectDB>> getSubjectsByUniversityId(
            @PathVariable("universityId") final Long universityId) {
        return new ResponseEntity<>(subjectRepository.findAllByUniversityId(universityId), OK);
    }

    @PostMapping("/subject/{username:.+}/{subjectName:.+}")
    public ResponseEntity<List<FileApi>> saveSubject(@PathVariable("subjectName") final String subjectName,
                                                     @PathVariable("username") final String username) {
        subjectService.save(username, subjectName);
        return new ResponseEntity<>(OK);
    }
}