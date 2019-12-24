package com.educationapp.server.files.repositories;

import java.util.List;

import com.educationapp.server.files.models.persistence.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {

    List<Subject> findAllByTeacherId(final Long teacherId);
}
