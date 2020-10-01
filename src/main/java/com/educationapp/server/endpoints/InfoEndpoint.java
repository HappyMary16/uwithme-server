package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.OK;

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

    @GetMapping(value = "/institutes")
    public ResponseEntity<?> getInstitutes() {
        return new ResponseEntity<>(instituteRepository.findAll(), OK);
    }

    @GetMapping(value = "/departments")
    public ResponseEntity<?> getDepartments() {
        return new ResponseEntity<>(departmentRepository.findAll(), OK);
    }

    @GetMapping(value = "/groups")
    public ResponseEntity<?> getStudyGroups() {
        return new ResponseEntity<>(studyGroupRepository.findAll(), OK);
    }

    @GetMapping(value = "/institutes/{universityId}")
    public ResponseEntity<?> getInstitutesByUniversityId(@PathVariable("universityId") final Long universityId) {
        return new ResponseEntity<>(instituteRepository.findAllByUniversityId(universityId), OK);
    }

    @GetMapping(value = "/departments/{universityId}")
    public ResponseEntity<?> getDepartmentsByUniversityId(@PathVariable("universityId") final Long universityId) {
        return new ResponseEntity<>(departmentRepository.findAllByUniversityId(universityId), OK);
    }
}
