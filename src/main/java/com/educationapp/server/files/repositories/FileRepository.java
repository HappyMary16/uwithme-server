package com.educationapp.server.files.repositories;

import java.util.List;

import com.educationapp.server.files.models.persistence.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {

    List<File> findBySubjectName(final String subjectName);
}
