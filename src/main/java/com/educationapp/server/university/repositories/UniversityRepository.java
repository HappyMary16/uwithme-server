package com.educationapp.server.university.repositories;

import com.educationapp.server.university.models.University;
import org.springframework.data.repository.CrudRepository;

public interface UniversityRepository extends CrudRepository<University, Long> {

}
