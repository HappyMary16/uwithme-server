package com.mborodin.uwm.repositories;

import java.util.List;
import java.util.Optional;

import com.mborodin.uwm.model.persistence.DepartmentDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentDb, Long> {

    List<DepartmentDb> findAllByUniversityId(Long universityId);

    List<DepartmentDb> findAllByInstituteId(Long instituteId);

    void deleteAllByInstituteId(Long instituteId);
}
