package com.educationapp.server.lecture.halls.repositoryes;

import com.educationapp.server.lecture.halls.models.LectureHallDb;
import org.springframework.data.repository.CrudRepository;

public interface LectureHallRepository extends CrudRepository<LectureHallDb, Long> {

}
