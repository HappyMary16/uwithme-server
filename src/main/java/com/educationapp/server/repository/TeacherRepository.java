package com.educationapp.server.repository;

import com.educationapp.server.models.TeachersDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends CrudRepository<TeachersDB, Long> {
}
