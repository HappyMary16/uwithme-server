package com.educationapp.server.lecture.halls.repositoryes;

import com.educationapp.server.lecture.halls.models.BuildingDb;
import org.springframework.data.repository.CrudRepository;

public interface BuildingsRepository extends CrudRepository<BuildingDb, Long> {

}
