package com.educationapp.server.files.repositories;

import java.util.List;

import com.educationapp.server.files.models.persistence.SubjectDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CrudRepository<SubjectDB, Long> {

    List<SubjectDB> findAllByTeacherId(final Long teacherId);
}
