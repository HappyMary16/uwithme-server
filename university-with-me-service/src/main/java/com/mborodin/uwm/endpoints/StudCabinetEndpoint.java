package com.mborodin.uwm.endpoints;

import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import com.mborodin.uwm.api.studcab.*;
import com.mborodin.uwm.clients.StudCabinetClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/studcab")
public class StudCabinetEndpoint {

    private final StudCabinetClient studCabinetClient;

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/students")
    public StudentInfo getStudentInfo(@RequestBody final AuthEntity authEntity) {
        return studCabinetClient.getStudentInfo(authEntity.getEmail(), authEntity.getPassword());
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/subjects/scores/{semesterId}")
    public List<SubjectScore> getSubjectScores(@PathVariable final int semesterId,
                                               @RequestBody final AuthEntity authEntity) {
        return studCabinetClient.getSubjectScores(authEntity.getEmail(), authEntity.getPassword(), semesterId);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/subjects/{semesterId}")
    public List<PlanSubject> getPlanSubjects(@PathVariable final int semesterId,
                                             @RequestBody final AuthEntity authEntity) {
        return studCabinetClient.getPlanSubjects(authEntity.getEmail(), authEntity.getPassword(), semesterId);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/students/rating/{semesterId}")
    public List<StudentScore> getStudentsRating(@PathVariable final int semesterId,
                                                @QueryParam("email") final String email,
                                                @QueryParam("password") final String password) {
        return studCabinetClient.getStudentScores(email, password, semesterId);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/students/debts")
    public List<StudentDebt> getStudentDebts(@RequestBody final AuthEntity authEntity) {
        return studCabinetClient.getStudentDebts(authEntity.getEmail(), authEntity.getPassword());
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping("/schedule/{groupId}")
    public Map<String, Map<String, Subject>> getStudentDebts(@PathVariable final String groupId) {
        return studCabinetClient.getSchedule(groupId);
    }
}
