package com.mborodin.uwm.client.client;

import java.util.List;

import com.mborodin.uwm.api.studcab.Credentials;
import com.mborodin.uwm.api.studcab.StudentDebt;
import com.mborodin.uwm.api.studcab.StudentInfo;
import com.mborodin.uwm.api.studcab.StudentScore;
import com.mborodin.uwm.api.studcab.SubjectScore;
import com.mborodin.uwm.api.studcab.Syllabus;
import com.mborodin.uwm.client.config.StudCabConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "studCabinetClient",
        url = "https://cabinet.kpi.kharkov.ua",
        configuration = {StudCabConfiguration.class})
public interface StudCabinetClient {

    @RequestMapping(method = RequestMethod.POST,
            value = "/servlets/servlet_kab_stud.php?page=1")
    StudentInfo getStudentInfo(Credentials credentials);

    @RequestMapping(method = RequestMethod.POST,
            value = "/servlets/servlet_kab_stud.php?page=2")
    List<SubjectScore> getSubjectScores(Credentials credentials,
                                        @RequestParam("semester") int semester);

    @RequestMapping(method = RequestMethod.POST,
            value = "/servlets/servlet_kab_stud.php?page=3")
    List<StudentDebt> getStudentDebts(Credentials credentials);

    @RequestMapping(method = RequestMethod.POST,
            value = "/servlets/servlet_kab_stud.php?page=4")
    List<Syllabus> getSyllabus(Credentials credentials,
                               @RequestParam("semester") int semester);

    @RequestMapping(method = RequestMethod.POST,
            value = "/servlets/servlet_kab_stud.php?page=5")
    List<StudentScore> getStudentScores(Credentials credentials,
                                        @RequestParam("semester") int semester);
}
