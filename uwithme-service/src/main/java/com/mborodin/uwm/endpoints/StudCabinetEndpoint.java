package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import com.mborodin.uwm.api.exceptions.IncorrectAuthDataException;
import com.mborodin.uwm.api.studcab.*;
import com.mborodin.uwm.client.client.StudCabinetClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/studcab")
public class StudCabinetEndpoint {

    private final StudCabinetClient studCabinetClient;

    @Secured("ROLE_STUDENT")
    @GetMapping("/students")
    public StudentInfo getStudentInfo(@QueryParam("email") final String email,
                                      @QueryParam("password") final String password) {
        final List<StudentInfo> studentInfos = studCabinetClient.getStudentInfo(email, password);

        if (studentInfos.isEmpty()) {
            throw new IncorrectAuthDataException(getLanguages());
        }

        return studentInfos.get(0);
    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/subjects/scores/{semesterId}")
    public List<SubjectScore> getSubjectScores(@PathVariable final int semesterId,
                                               @QueryParam("email") final String email,
                                               @QueryParam("password") final String password) {
        return studCabinetClient.getSubjectScores(email, password, semesterId);
    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/subjects/{semesterId}")
    public List<PlanSubject> getPlanSubjects(@PathVariable final int semesterId,
                                             @QueryParam("email") final String email,
                                             @QueryParam("password") final String password) {
        return studCabinetClient.getPlanSubjects(email, password, semesterId);
    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/students/rating/{semesterId}")
    public List<StudentScore> getStudentsRating(@PathVariable final int semesterId,
                                                @QueryParam("email") final String email,
                                                @QueryParam("password") final String password) {
        return studCabinetClient.getStudentScores(email, password, semesterId);
    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/students/debts")
    public List<StudentDebt> getStudentDebts(@QueryParam("email") final String email,
                                             @QueryParam("password") final String password) {
        return studCabinetClient.getStudentDebts(email, password);
    }

    @Secured("ROLE_STUDENT")
    @GetMapping("/schedule/{groupId}")
    public Map<String, Map<String, Subject>> getStudentDebts(@PathVariable final String groupId) {
        return studCabinetClient.getSchedule(groupId);
    }
}
