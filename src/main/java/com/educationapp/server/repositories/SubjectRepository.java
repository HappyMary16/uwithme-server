package com.educationapp.server.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.models.persistence.SubjectDB;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CrudRepository<SubjectDB, Long> {

    List<SubjectDB> findAllByTeacherId(final Long teacherId);

    Optional<SubjectDB> findByNameAndTeacherId(final String name, final Long teacherId);

    @Query(value = "SELECT * FROM subjects JOIN users ON subjects.teacher_id = users.id WHERE users.university_id = " +
            ":universityId",
            nativeQuery = true)
    List<SubjectDB> findAllByUniversityId(@Param("universityId") Long universityId);
}