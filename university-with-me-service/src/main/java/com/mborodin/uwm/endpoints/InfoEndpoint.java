package com.mborodin.uwm.endpoints;

import static org.springframework.http.HttpStatus.OK;

import com.mborodin.uwm.repositories.DepartmentRepository;
import com.mborodin.uwm.repositories.InstituteRepository;
import com.mborodin.uwm.repositories.StudyGroupDataRepository;
import com.mborodin.uwm.repositories.UniversityRepository;
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
        return new ResponseEntity<>(departmentRepository.findAllByInstituteId(instituteId), OK);
    }

    @GetMapping(value = "/groups/{departmentId}")
    public ResponseEntity<?> getStudyGroups(@PathVariable("departmentId") final Long departmentId) {
        return new ResponseEntity<>(studyGroupRepository.findAllByVisibleAndDepartmentId(true, departmentId), OK);
    }
}
