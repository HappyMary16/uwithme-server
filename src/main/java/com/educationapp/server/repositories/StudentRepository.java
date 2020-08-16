package com.educationapp.server.repositories;

import com.educationapp.server.models.persistence.StudentDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<StudentDB, Long> {

}
