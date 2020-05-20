package com.educationapp.server.university.repositories;

import java.util.List;

import com.educationapp.server.university.models.StudyGroupDataDb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupDataRepository extends CrudRepository<StudyGroupDataDb, Long> {

    List<StudyGroupDataDb> findAllByUniversityId(Long universityId);
}
