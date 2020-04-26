package com.educationapp.server.university.repositories;

import com.educationapp.server.university.models.University;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends CrudRepository<University, Long> {

}
