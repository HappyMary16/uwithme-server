package com.educationapp.server.repository;

import com.educationapp.server.model.persistence.TeacherDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends CrudRepository<TeacherDB, Long> {

}
