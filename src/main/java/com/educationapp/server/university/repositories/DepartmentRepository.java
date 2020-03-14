package com.educationapp.server.university.repositories;

import java.util.List;

import com.educationapp.server.university.models.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {


    @Query(value = "SELECT * FROM departments LEFT JOIN institutes ON departments.institute_id = institutes.id WHERE " +
            "institutes.university_id = :universityId",
            nativeQuery = true)
    List<Department> findDepartmentsByUniversityId(@Param("universityId") Integer universityId);
}
