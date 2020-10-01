package com.educationapp.server.endpoints;

import static com.educationapp.server.enums.Role.ADMIN;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.models.persistence.SubjectDB;
import com.educationapp.server.repositories.SubjectRepository;
import com.educationapp.server.security.UserContextHolder;
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
        final UserApi user = UserContextHolder.getUser();
        if (user.getRole().equals(ADMIN.getId())) {
            return new ResponseEntity<>(subjectRepository.findAllByUniversityId(user.getUniversityId()), OK);
        } else {
            return new ResponseEntity<>(subjectService.findSubjectsByTeacherUsername(), OK);
        }
    }

    @PostMapping("/{username:.+}/{subjectName:.+}")
    public ResponseEntity<?> saveSubject(@PathVariable("subjectName") final String subjectName,
                                         @PathVariable("username") final String username) {
        subjectService.save(username, subjectName);
        return new ResponseEntity<>(OK);
    }
}
