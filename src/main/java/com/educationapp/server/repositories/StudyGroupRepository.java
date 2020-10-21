package com.educationapp.server.repositories;

import java.util.List;

import com.educationapp.server.models.persistence.StudyGroupDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupRepository extends CrudRepository<StudyGroupDb, Long> {

    @Query(value = "SELECT * FROM study_groups sg JOIN departments d ON  sg.department_id = d.id " +
            "LEFT JOIN institutes i ON d.institute_id = i.id WHERE i.university_id = :universityId",
            nativeQuery = true)
    List<StudyGroupDb> findAllByUniversityId(@Param("universityId") Long universityId);

    @Query(value = "SELECT * FROM study_groups sg WHERE sg.id IN :ids", nativeQuery = true)
    List<StudyGroupDb> findAllByIds(List<Long> ids);

    @Query(value = "SELECT * FROM study_groups sg WHERE sg.name IN :names", nativeQuery = true)
    List<StudyGroupDb> findAllByNames(List<String> names);
}
