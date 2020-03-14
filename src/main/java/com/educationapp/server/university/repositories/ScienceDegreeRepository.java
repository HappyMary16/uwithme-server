package com.educationapp.server.university.repositories;

import com.educationapp.server.university.models.ScienceDegree;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScienceDegreeRepository extends CrudRepository<ScienceDegree, Long> {

}
