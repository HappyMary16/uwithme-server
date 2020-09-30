package com.educationapp.server.endpoints;

import java.util.List;

import com.educationapp.server.models.persistence.DepartmentDb;
import com.educationapp.server.models.persistence.InstituteDb;
import com.educationapp.server.models.persistence.StudyGroupDataDb;
import com.educationapp.server.models.persistence.UniversityDb;
import com.educationapp.server.repositories.DepartmentRepository;
import com.educationapp.server.repositories.InstituteRepository;
import com.educationapp.server.repositories.StudyGroupDataRepository;
import com.educationapp.server.repositories.UniversityRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/info")
public class InfoEndpoint {

    private final InstituteRepository instituteRepository;
    private final DepartmentRepository departmentRepository;
    private final StudyGroupDataRepository studyGroupRepository;
    private final UniversityRepository universityRepository;

    @RequestMapping(value = "/universities", method = RequestMethod.GET)
    public List<UniversityDb> getUniversities() {
        return (List<UniversityDb>) universityRepository.findAll();
    }

    @RequestMapping(value = "/institutes", method = RequestMethod.GET)
    public List<InstituteDb> getInstitutes() {
        return (List<InstituteDb>) instituteRepository.findAll();
    }

    @RequestMapping(value = "/departments", method = RequestMethod.GET)
    public List<DepartmentDb> getDepartments() {
        return (List<DepartmentDb>) departmentRepository.findAll();
    }

    @RequestMapping(value = "/studyGroups", method = RequestMethod.GET)
    public List<StudyGroupDataDb> getStudyGroups() {
        return (List<StudyGroupDataDb>) studyGroupRepository.findAll();
    }

    @RequestMapping(value = "/institutes/{universityId}", method = RequestMethod.GET)
    public List<InstituteDb> getInstitutesByUniversityId(@PathVariable("universityId") final Long universityId) {
        return instituteRepository.findAllByUniversityId(universityId);
    }

    @RequestMapping(value = "/departments/{universityId}", method = RequestMethod.GET)
    public List<DepartmentDb> getDepartmentsByUniversityId(@PathVariable("universityId") final Long universityId) {
        return departmentRepository.findAllByUniversityId(universityId);
    }
}
