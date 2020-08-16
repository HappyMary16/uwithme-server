package com.educationapp.server.repositories;

import java.util.List;

import com.educationapp.server.models.persistence.StudyGroupDataDb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupDataRepository extends CrudRepository<StudyGroupDataDb, Long> {

    List<StudyGroupDataDb> findAllByUniversityId(Long universityId);
}
