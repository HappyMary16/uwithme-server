package com.educationapp.server.users.repositories;

import com.educationapp.server.users.model.persistence.TeacherDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends CrudRepository<TeacherDB, Long> {

}
