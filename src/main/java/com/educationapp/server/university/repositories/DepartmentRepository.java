package com.educationapp.server.university.repositories;

import com.educationapp.server.university.models.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

}
