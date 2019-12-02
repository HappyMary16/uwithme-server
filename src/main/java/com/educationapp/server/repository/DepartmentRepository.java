package com.educationapp.server.repository;

import com.educationapp.server.model.domain.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

}
