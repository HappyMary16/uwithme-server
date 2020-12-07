package com.educationapp.server.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.models.persistence.DepartmentDb;
import com.educationapp.server.models.persistence.InstituteDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentDb, Long> {

    @Query("SELECT department " +
            "FROM DepartmentDb department " +
            "WHERE department.institute.universityId = :universityId")
    List<DepartmentDb> findAllByUniversityId(@Param("universityId") Long universityId);

    List<DepartmentDb> findAllByInstitute(InstituteDb institute);

    Optional<DepartmentDb> findByInstituteIdAndName(Long instituteId, String name);

    default DepartmentDb getProxyByIdIfExist(final Long id) {
        return id != null
                ? getOne(id)
                : null;
    }
}
