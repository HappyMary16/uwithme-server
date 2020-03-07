package com.educationapp.server.university.repositories;

import java.util.List;

import com.educationapp.server.university.models.Institute;
import org.springframework.data.repository.CrudRepository;

public interface InstituteRepository extends CrudRepository<Institute, Long> {

    List<Institute> findByUniversityId(final Integer universityId);
}
