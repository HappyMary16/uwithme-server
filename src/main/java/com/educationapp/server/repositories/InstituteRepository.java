package com.educationapp.server.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.models.persistence.InstituteDb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituteRepository extends CrudRepository<InstituteDb, Long> {

    List<InstituteDb> findAllByUniversityId(final Long universityId);

    Optional<InstituteDb> findByUniversityIdAndName(final Long universityId, final String name);
}
