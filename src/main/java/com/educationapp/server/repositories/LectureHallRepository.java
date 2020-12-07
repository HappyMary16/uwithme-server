package com.educationapp.server.repositories;

import java.util.List;

import com.educationapp.server.models.persistence.LectureHallDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LectureHallRepository extends CrudRepository<LectureHallDb, Long> {

    @Query("SELECT lectureHall " +
            "FROM LectureHallDb lectureHall " +
            "WHERE lectureHall.building.universityId = :universityId")
    List<LectureHallDb> findAllByUniversityId(@Param("universityId") Long universityId);
}
