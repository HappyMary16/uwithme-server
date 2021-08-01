package com.mborodin.uwm.clients;

import java.util.List;
import java.util.Map;

import com.mborodin.uwm.api.studcab.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "studCabinetClient", url = "https://schedule.kpi.kharkov.ua/json/")
public interface StudCabinetClient {

    @RequestMapping(method = RequestMethod.GET,
            value = "/kabinet?email={email}&pass={password}&page=1")
    StudentInfo getStudentInfo(@PathVariable("email") String email,
                               @PathVariable("password") String password);

    @RequestMapping(method = RequestMethod.GET,
            value = "/kabinet?email={email}&pass={password}&page=2&semestr={semester}")
    List<SubjectScore> getSubjectScores(@PathVariable("email") String email,
                                        @PathVariable("password") String password,
                                        @PathVariable("semester") int semester);

    @RequestMapping(method = RequestMethod.GET,
            value = "/kabinet?email={email}&pass={password}&page=5&semestr={semester}")
    List<StudentScore> getStudentScores(@PathVariable("email") String email,
                                        @PathVariable("password") String password,
                                        @PathVariable("semester") int semester);

    @RequestMapping(method = RequestMethod.GET,
            value = "/kabinet?email={email}&pass={password}&page=3")
    List<StudentDebt> getStudentDebts(@PathVariable("email") String email,
                                      @PathVariable("password") String password);

    @RequestMapping(method = RequestMethod.GET,
            value = "/kabinet?email={email}&pass={password}&page=4&semestr={semester}")
    List<PlanSubject> getPlanSubjects(@PathVariable("email") String email,
                                      @PathVariable("password") String password,
                                      @PathVariable("semester") int semester);

    @RequestMapping(method = RequestMethod.GET,
            value = "/Schedule/{groupId}")
    Map<String, Map<String, Subject>> getSchedule(@PathVariable("groupId") String groupId);
}
