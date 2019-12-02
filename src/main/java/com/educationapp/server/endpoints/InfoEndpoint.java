package com.educationapp.server.endpoints;

import java.util.List;

import com.educationapp.server.model.domain.Department;
import com.educationapp.server.model.domain.Institute;
import com.educationapp.server.model.domain.StudyGroup;
import com.educationapp.server.repository.DepartmentRepository;
import com.educationapp.server.repository.InstituteRepository;
import com.educationapp.server.repository.StudyGroupRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
@CrossOrigin("*")
public class InfoEndpoint implements InitializingBean {

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @RequestMapping(value = "/institutes", method = RequestMethod.GET)
    public List<Institute> getInstitutes() {
        return (List<Institute>) instituteRepository.findAll();
    }

    @RequestMapping(value = "/departments", method = RequestMethod.GET)
    public List<Department> getDepartments() {
        return (List<Department>) departmentRepository.findAll();
    }

    @RequestMapping(value = "/studyGroups", method = RequestMethod.GET)
    public List<StudyGroup> getStudyGroups() {
        return (List<StudyGroup>) studyGroupRepository.findAll();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        instituteRepository.save(new Institute("I"));
//        departmentRepository.save(new Department("KMPS", 1L));
//        studyGroupRepository.save(new StudyGroup("I-26b", 1L, 4));
    }
}
