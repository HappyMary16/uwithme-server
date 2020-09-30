package com.educationapp.server.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.models.persistence.StudyGroupDataDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupDataRepository extends CrudRepository<StudyGroupDataDb, Long> {

    Optional<StudyGroupDataDb> findById(Long id);

    @Query(value = "SELECT * FROM study_groups sg " +
            "JOIN departments d ON sg.department_id = d.id " +
            "JOIN institutes i on d.institute_id = i.id " +
            "WHERE i.university_id = :universityId",
            nativeQuery = true)
    List<StudyGroupDataDb> findAllByUniversityId(Long universityId);

    List<StudyGroupDataDb> findAllByTeacherId(Long teacherId);
}
