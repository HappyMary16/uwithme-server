package com.educationapp.server.university.endpoints;

import java.util.List;

import com.educationapp.server.university.models.Department;
import com.educationapp.server.university.models.Institute;
import com.educationapp.server.university.models.StudyGroup;
import com.educationapp.server.university.repositories.DepartmentRepository;
import com.educationapp.server.university.repositories.InstituteRepository;
import com.educationapp.server.university.repositories.StudyGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
@CrossOrigin("*")
public class InfoEndpoint {

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

    @RequestMapping(value = "/institutes/{universityId}", method = RequestMethod.GET)
    public List<Institute> getInstitutesByUniversityId(@PathVariable("universityId") final Integer universityId) {
        return instituteRepository.findAllByUniversityId(universityId);
    }

    @RequestMapping(value = "/departments/{universityId}", method = RequestMethod.GET)
    public List<Department> getDepartmentsByUniversityId(@PathVariable("universityId") final Integer universityId) {
        return departmentRepository.findAllByUniversityId(universityId);
    }

    @RequestMapping(value = "/studyGroups/{universityId}", method = RequestMethod.GET)
    public List<StudyGroup> getStudyGroupsByUniversityId(@PathVariable("universityId") final Integer universityId) {
        return (List<StudyGroup>) studyGroupRepository.findAll();//findAllByUniversityId(universityId);
    }
}
