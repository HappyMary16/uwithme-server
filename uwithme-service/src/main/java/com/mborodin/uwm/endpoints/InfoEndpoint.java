package com.mborodin.uwm.endpoints;

import java.util.List;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.api.structure.InstituteApi;
import com.mborodin.uwm.model.mapper.DepartmentMapper;
import com.mborodin.uwm.model.mapper.InstituteMapper;
import com.mborodin.uwm.model.persistence.StudyGroupDataDb;
import com.mborodin.uwm.model.persistence.TenantDb;
import com.mborodin.uwm.repositories.StudyGroupDataRepository;
import com.mborodin.uwm.repositories.TenantDepartmentRepository;
import com.mborodin.uwm.repositories.UniversityRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/info")
public class InfoEndpoint {

    private final StudyGroupDataRepository studyGroupRepository;
    private final UniversityRepository universityRepository;
    private final TenantDepartmentRepository tenantDepartmentRepository;

    private final InstituteMapper instituteMapper;
    private final DepartmentMapper departmentMapper;

    @GetMapping(value = "/universities")
    public Iterable<TenantDb> getUniversities() {
        return universityRepository.findAll();
    }

    @GetMapping(value = "/institutes/{universityId}")
    public List<InstituteApi> getInstitutes(@PathVariable("universityId") final Long universityId) {
        return tenantDepartmentRepository.findAllByTenantIdAndMainDepartmentIdIsNull(universityId)
                                         .stream()
                                         .map(instituteMapper::toInstituteApi)
                                         .collect(Collectors.toList());
    }

    @GetMapping(value = "/departments/{instituteId}")
    public List<DepartmentApi> getDepartments(@PathVariable("instituteId") final String instituteId) {
        return tenantDepartmentRepository.findAllByMainDepartmentId(instituteId)
                                         .stream()
                                         .map(departmentMapper::toDepartmentApi)
                                         .collect(Collectors.toList());
    }

    @GetMapping(value = "/groups/{departmentId}")
    public List<StudyGroupDataDb> getStudyGroups(@PathVariable("departmentId") final String departmentId) {
        return studyGroupRepository.findAllByVisibleAndDepartmentId(true, departmentId);
    }
}
