package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getId;

import java.util.List;
import java.util.Objects;

import javax.ws.rs.ForbiddenException;

import com.mborodin.uwm.model.persistence.SubjectDB;
import com.mborodin.uwm.repositories.SubjectRepository;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/subjects")
public class SubjectEndpoint {

    private final SubjectService subjectService;
    private final SubjectRepository subjectRepository;

    @Secured("ROLE_ADMIN")
    @GetMapping
    public List<SubjectDB> getSubjects() {
        final Long universityId = UserContextHolder.getUniversityId();
        return subjectRepository.findAllByUniversityId(universityId);
    }

    @Secured({"ROLE_TEACHER", "ROLE_STUDENT"})
    @GetMapping("/{userId}")
    public List<SubjectDB> getSubjects(@PathVariable("userId") final String userId) {
        if (Objects.equals(userId, getId())) {
            return subjectService.findUsersSubjects();
        }

        throw new ForbiddenException();
    }

    @Secured("ROLE_TEACHER")
    @PostMapping("/{userId}/{subjectName}")
    public SubjectDB saveSubject(@PathVariable("subjectName") final String subjectName,
                                 @PathVariable("userId") final String userId) {
        return subjectService.save(userId, subjectName);
    }
}
