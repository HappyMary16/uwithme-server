package com.mborodin.uwm.repositories;

import java.util.List;

import com.mborodin.uwm.model.persistence.LectureHallDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LectureHallRepository extends CrudRepository<LectureHallDb, Long> {

    @Query("SELECT lectureHall " +
            "FROM LectureHallDb lectureHall " +
            "JOIN BuildingDb building " +
            "ON building.id = lectureHall.buildingId " +
            "WHERE building.universityId = :universityId")
    List<LectureHallDb> findAllByUniversityId(@Param("universityId") Long universityId);
}
