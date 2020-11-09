package com.educationapp.server.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.models.persistence.DepartmentDb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<DepartmentDb, Long> {

    @Query(value = "SELECT * FROM departments LEFT JOIN institutes ON departments.institute_id = institutes.id WHERE " +
            "institutes.university_id = :universityId",
            nativeQuery = true)
    List<DepartmentDb> findAllByUniversityId(@Param("universityId") Long universityId);

    Optional<DepartmentDb> findByInstituteIdAndName(Long instituteId, String name);

    default DepartmentDb getProxyByIdIfExist(final Long id) {
        return id != null
                ? getOne(id)
                : null;
    }
}
