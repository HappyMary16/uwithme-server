package com.educationapp.server.endpoints;

import com.educationapp.server.models.api.admin.AddDepartmentApi;
import com.educationapp.server.models.persistence.DepartmentDb;
import com.educationapp.server.models.persistence.InstituteDb;
import com.educationapp.server.repositories.DepartmentRepository;
import com.educationapp.server.repositories.InstituteRepository;
import com.educationapp.server.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@AllArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentEndpoint {

    private final InstituteRepository instituteRepository;

    private final DepartmentRepository departmentRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addDepartment(@RequestBody final AddDepartmentApi addDepartmentApi) {
        final Long universityId = UserContextHolder.getUniversityId();
        final String instituteName = addDepartmentApi.getInstituteName();

        final InstituteDb institute = instituteRepository.findByUniversityIdAndName(universityId, instituteName)
                .orElseGet(() -> InstituteDb.builder()
                        .universityId(universityId)
                        .name(instituteName)
                        .build());
        final DepartmentDb department = DepartmentDb.builder()
                .name(addDepartmentApi.getDepartmentName())
                .institute(institute)
                .build();

        return new ResponseEntity<>(departmentRepository.save(department), OK);
    }

    @GetMapping
    public ResponseEntity<?> getDepartmentsByUniversityId() {
        final Long universityId = UserContextHolder.getUniversityId();
        return new ResponseEntity<>(departmentRepository.findAllByUniversityId(universityId), OK);
    }

    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER')")
    @GetMapping("/user")
    public ResponseEntity<DepartmentDb> getDepartment() {
        final var departmentId = UserContextHolder.getUserDb().getDepartmentId();

        return Optional.ofNullable(departmentId)
                .flatMap(departmentRepository::findById)
                .map(group -> new ResponseEntity<>(group, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }
}