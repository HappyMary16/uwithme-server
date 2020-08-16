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

    @Query(value = "SELECT DISTINCT sg.id, sg.name, department_id, course FROM study_groups sg " +
            "LEFT JOIN schedule s ON  sg.id = s.study_group_id " +
            "LEFT JOIN subjects su ON s.study_group_id = su.id WHERE su.teacher_id = :teacherId",
            nativeQuery = true)
    List<StudyGroupDb> findAllByTeacherId(@Param("teacherId") Long universityId);
}
