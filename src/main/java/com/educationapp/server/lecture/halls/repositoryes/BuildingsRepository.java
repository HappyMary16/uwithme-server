package com.educationapp.server.lecture.halls.repositoryes;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.lecture.halls.models.BuildingDb;
import org.springframework.data.repository.CrudRepository;

public interface BuildingsRepository extends CrudRepository<BuildingDb, Long> {

    List<BuildingDb> findAllByUniversityId(final Long universityId);

    Optional<BuildingDb> findByUniversityIdAndName(final Long universityId, final String name);
}
