package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.enums.Role.ADMIN;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import com.mborodin.uwm.models.persistence.SubjectDB;
import com.mborodin.uwm.repositories.SubjectRepository;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/subjects")
public class SubjectEndpoint {

    private final SubjectService subjectService;
    private final SubjectRepository subjectRepository;

    @GetMapping
    public ResponseEntity<List<SubjectDB>> getSubjects() {
        if (ADMIN.equals(UserContextHolder.getRole())) {
            final Long universityId = UserContextHolder.getUniversityId();
            return new ResponseEntity<>(subjectRepository.findAllByUniversityId(universityId), OK);
        } else {
            return new ResponseEntity<>(subjectService.findUsersSubjects(), OK);
        }
    }

    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping("/{username:.+}/{subjectName:.+}")
    public ResponseEntity<?> saveSubject(@PathVariable("subjectName") final String subjectName,
                                         @PathVariable("username") final String username) {
        subjectService.save(username, subjectName);
        return new ResponseEntity<>(OK);
    }
}
