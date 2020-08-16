package com.educationapp.server.repositories;

import com.educationapp.server.models.persistence.ScienceDegreeDb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScienceDegreeRepository extends CrudRepository<ScienceDegreeDb, Long> {

}
