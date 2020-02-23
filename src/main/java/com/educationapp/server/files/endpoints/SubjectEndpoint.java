package com.educationapp.server.files.endpoints;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import com.educationapp.server.common.api.FileApi;
import com.educationapp.server.files.models.persistence.SubjectDB;
import com.educationapp.server.files.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class SubjectEndpoint {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/subjects/{username:.+}")
    public ResponseEntity<List<SubjectDB>> getSubjects(@PathVariable("username") final String username) {
        return new ResponseEntity<>(subjectService.findSubjectsByTeacherUsername(username), OK);
    }

    @PostMapping("/subject/{username:.+}/{subjectName:.+}")
    public ResponseEntity<List<FileApi>> saveSubject(@PathVariable("subjectName") final String subjectName,
                                                     @PathVariable("username") final String username) {
        subjectService.save(username, subjectName);
        return new ResponseEntity<>(OK);
    }
}
