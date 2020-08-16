package com.educationapp.server.repositories;

import com.educationapp.server.models.persistence.UniversityDb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends CrudRepository<UniversityDb, Long> {

}
