package com.educationapp.server.repositories;

import java.util.List;

import com.educationapp.server.models.persistence.StudyGroupDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupRepository extends CrudRepository<StudyGroupDb, Long> {

    @Query("SELECT studyGroup " +
            "FROM StudyGroupDb studyGroup " +
            "WHERE studyGroup.id IN :ids")
    List<StudyGroupDb> findAllByIds(List<Long> ids);
}