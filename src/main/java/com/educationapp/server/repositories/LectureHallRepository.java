package com.educationapp.server.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.models.persistence.LectureHallDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LectureHallRepository extends CrudRepository<LectureHallDb, Long> {

    @Query(value = "SELECT * FROM lecture_halls lh JOIN buildings b on lh.building_id = b.id WHERE b.university_id = " +
            ":universityId",
            nativeQuery = true)
    List<LectureHallDb> findAllByUniversityId(@Param("universityId") Long universityId);

    Optional<LectureHallDb> findByName(String name);
}
