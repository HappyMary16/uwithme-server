package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.OK;

import com.educationapp.server.models.persistence.DepartmentDb;
import com.educationapp.server.models.persistence.InstituteDb;
import com.educationapp.server.repositories.DepartmentRepository;
import com.educationapp.server.repositories.InstituteRepository;
import com.educationapp.server.repositories.StudyGroupDataRepository;
import com.educationapp.server.repositories.UniversityRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/info")
public class InfoEndpoint {

    private final InstituteRepository instituteRepository;

    private final DepartmentRepository departmentRepository;

    private final StudyGroupDataRepository studyGroupRepository;

    private final UniversityRepository universityRepository;

    @GetMapping(value = "/universities")
    public ResponseEntity<?> getUniversities() {
        return new ResponseEntity<>(universityRepository.findAll(), OK);
    }

    @GetMapping(value = "/institutes/{universityId}")
    public ResponseEntity<?> getInstitutes(@PathVariable("universityId") final Long universityId) {
        return new ResponseEntity<>(instituteRepository.findAllByUniversityId(universityId), OK);
    }

    @GetMapping(value = "/departments/{instituteId}")
    public ResponseEntity<?> getDepartments(@PathVariable("instituteId") final Long instituteId) {
        final InstituteDb instituteDb = instituteRepository.getProxyByIdIfExist(instituteId);
        return new ResponseEntity<>(departmentRepository.findAllByInstitute(instituteDb), OK);
    }

    @GetMapping(value = "/groups/{departmentId}")
    public ResponseEntity<?> getStudyGroups(@PathVariable("departmentId") final Long departmentId) {
        final DepartmentDb department = departmentRepository.getProxyByIdIfExist(departmentId);
        return new ResponseEntity<>(studyGroupRepository.findAllByIsVisibleAndDepartment(true, department), OK);
    }
}
