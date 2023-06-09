package com.mborodin.uwm.endpoints;

import static com.mborodin.uwm.security.UserContextHolder.getLanguages;

import java.util.List;

import com.mborodin.uwm.api.exceptions.IncorrectAuthDataException;
import com.mborodin.uwm.api.exceptions.UnknownException;
import com.mborodin.uwm.api.studcab.Credentials;
import com.mborodin.uwm.api.studcab.StudentDebt;
import com.mborodin.uwm.api.studcab.StudentInfo;
import com.mborodin.uwm.api.studcab.StudentScore;
import com.mborodin.uwm.api.studcab.SubjectScore;
import com.mborodin.uwm.api.studcab.Syllabus;
import com.mborodin.uwm.client.client.StudCabinetClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/studcab")
public class StudCabinetEndpoint {

    private final StudCabinetClient studCabinetClient;

    @Secured("ROLE_STUDENT")
    @PostMapping("/students")
    public StudentInfo getStudentInfo(@RequestBody final Credentials credentials) {
        try {
            final StudentInfo studentInfo = studCabinetClient.getStudentInfo(credentials);
            if (studentInfo == null) {
                throw new IncorrectAuthDataException(getLanguages());
            }
            return studentInfo;
        } catch (final Throwable ignore) {
            throw new UnknownException(getLanguages());
        }
    }

    @Secured("ROLE_STUDENT")
    @PostMapping("/scores/{semesterId}")
    public List<SubjectScore> getSubjectScores(@PathVariable final int semesterId,
                                               @RequestBody final Credentials credentials) {
        return studCabinetClient.getSubjectScores(credentials, semesterId);
    }

    @Secured("ROLE_STUDENT")
    @PostMapping("/rating/{semesterId}")
    public List<StudentScore> getStudentsRating(@PathVariable final int semesterId,
                                                @RequestBody final Credentials credentials) {
        return studCabinetClient.getStudentScores(credentials, semesterId);
    }

    @Secured("ROLE_STUDENT")
    @PostMapping("/debts")
    public List<StudentDebt> getStudentDebts(@RequestBody final Credentials credentials) {
        return studCabinetClient.getStudentDebts(credentials);
    }

    @Secured("ROLE_STUDENT")
    @PostMapping("/syllabus/{semesterId}")
    public List<Syllabus> getSyllabus(@PathVariable final int semesterId,
                                               @RequestBody final Credentials credentials) {
        return studCabinetClient.getSyllabus(credentials, semesterId);
    }
}
