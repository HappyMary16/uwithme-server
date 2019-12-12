package com.educationapp.server.university.data.repositories;

import com.educationapp.server.university.data.models.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

}
