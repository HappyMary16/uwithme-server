package com.educationapp.server.repository;

import com.educationapp.server.model.persistence.StudentDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<StudentDB, Long> {
}
