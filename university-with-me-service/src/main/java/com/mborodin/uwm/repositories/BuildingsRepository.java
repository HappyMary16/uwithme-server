package com.mborodin.uwm.repositories;

import java.util.List;
import java.util.Optional;

import com.mborodin.uwm.models.persistence.BuildingDb;
import org.springframework.data.repository.CrudRepository;

public interface BuildingsRepository extends CrudRepository<BuildingDb, Long> {

    List<BuildingDb> findAllByUniversityId(final Long universityId);

    Optional<BuildingDb> findByUniversityIdAndName(final Long universityId, final String name);
}
