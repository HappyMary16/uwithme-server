package com.educationapp.server.university.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.university.models.Institute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituteRepository extends CrudRepository<Institute, Long> {

    List<Institute> findAllByUniversityId(final Long universityId);

    Optional<Institute> findByUniversityIdAndName(final Long universityId, final String name);
}
