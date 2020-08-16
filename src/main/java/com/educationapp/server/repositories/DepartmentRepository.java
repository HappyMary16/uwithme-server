package com.educationapp.server.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.models.persistence.DepartmentDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends CrudRepository<DepartmentDb, Long> {

    @Query(value = "SELECT * FROM departments LEFT JOIN institutes ON departments.institute_id = institutes.id WHERE " +
            "institutes.university_id = :universityId",
            nativeQuery = true)
    List<DepartmentDb> findAllByUniversityId(@Param("universityId") Long universityId);

    Optional<DepartmentDb> findByInstituteIdAndName(Long instituteId, String name);
}
