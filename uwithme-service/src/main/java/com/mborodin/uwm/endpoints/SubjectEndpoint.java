package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getId;

import java.util.List;
import java.util.Objects;

import javax.ws.rs.QueryParam;

import com.mborodin.uwm.api.SubjectApi;
import com.mborodin.uwm.security.UserContextHolder;
import com.mborodin.uwm.services.SubjectService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/subjects")
public class SubjectEndpoint {

    private final SubjectService subjectService;

    @GetMapping
    public List<SubjectApi> getSubjects(@QueryParam("userId") final String userId) {
        if (Objects.equals(userId, getId())) {
            return subjectService.findUsersSubjects();
        }

        final Long universityId = UserContextHolder.getUniversityId();
        return subjectService.findSubjects(universityId);
    }

    @Secured({"ROLE_TEACHER", "ROLE_ADMIN"})
    @PostMapping
    public SubjectApi saveSubject(@RequestBody final SubjectApi subject) {
        return subjectService.save(subject.getTeacherId(), subject.getSubjectName());
    }
}
