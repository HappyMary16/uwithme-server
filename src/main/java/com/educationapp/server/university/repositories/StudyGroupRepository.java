package com.educationapp.server.university.repositories;

import com.educationapp.server.university.models.StudyGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupRepository extends CrudRepository<StudyGroup, Long> {

}
