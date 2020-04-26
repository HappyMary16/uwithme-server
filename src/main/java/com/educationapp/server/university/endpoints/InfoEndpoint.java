package com.educationapp.server.university.endpoints;

import java.util.List;

import com.educationapp.server.university.models.Department;
import com.educationapp.server.university.models.Institute;
import com.educationapp.server.university.models.StudyGroup;
import com.educationapp.server.university.models.University;
import com.educationapp.server.university.repositories.DepartmentRepository;
import com.educationapp.server.university.repositories.InstituteRepository;
import com.educationapp.server.university.repositories.StudyGroupRepository;
import com.educationapp.server.university.repositories.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
public class InfoEndpoint {

    @Autowired
    private InstituteRepository instituteRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @RequestMapping(value = "/universities", method = RequestMethod.GET)
    public List<University> getUniversities() {
        return (List<University>) universityRepository.findAll();
    }

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
    public List<Institute> getInstitutesByUniversityId(@PathVariable("universityId") final Long universityId) {
        return instituteRepository.findAllByUniversityId(universityId);
    }

    @RequestMapping(value = "/departments/{universityId}", method = RequestMethod.GET)
    public List<Department> getDepartmentsByUniversityId(@PathVariable("universityId") final Long universityId) {
        return departmentRepository.findAllByUniversityId(universityId);
    }
}
