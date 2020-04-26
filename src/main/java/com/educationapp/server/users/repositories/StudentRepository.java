package com.educationapp.server.users.repositories;

import com.educationapp.server.users.model.persistence.StudentDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends CrudRepository<StudentDB, Long> {

}
