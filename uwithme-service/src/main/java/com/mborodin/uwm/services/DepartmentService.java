package com.mborodin.uwm.services;

import com.mborodin.uwm.api.structure.DepartmentApi;
import com.mborodin.uwm.model.mapper.DepartmentMapper;
import com.mborodin.uwm.model.persistence.DepartmentDb;
import com.mborodin.uwm.repositories.DepartmentRepository;
import com.mborodin.uwm.repositories.StudyGroupDataRepository;
import com.mborodin.uwm.repositories.StudyGroupRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class DepartmentService {

    private final StudyGroupDataRepository studyGroupDataRepository;
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentApi getById(final long departmentId) {
        return departmentRepository.findById(departmentId)
                                   .map(departmentMapper::toDepartmentApi)
                                   .orElse(null);
    }

    @Transactional
    public void deleteByInstituteId(final long instituteId) {
        departmentRepository.findAllByInstituteId(instituteId)
                            .stream()
                            .map(DepartmentDb::getId)
                            .forEach(studyGroupDataRepository::deleteAllByDepartmentId);

        departmentRepository.deleteAllByInstituteId(instituteId);
    }

    @Transactional
    public void deleteDepartment(final long departmentId) {
        studyGroupDataRepository.deleteAllByDepartmentId(departmentId);
        departmentRepository.deleteById(departmentId);
    }
}
