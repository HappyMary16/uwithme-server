package com.educationapp.server.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.models.persistence.BuildingDb;
import org.springframework.data.repository.CrudRepository;

public interface BuildingsRepository extends CrudRepository<BuildingDb, Long> {

    List<BuildingDb> findAllByUniversityId(final Long universityId);

    Optional<BuildingDb> findByUniversityIdAndName(final Long universityId, final String name);
}