package com.educationapp.server.repositories;

import com.educationapp.server.models.persistence.TeacherDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends CrudRepository<TeacherDB, Long> {

}
