package com.educationapp.server.files.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.files.models.persistence.FileDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<FileDB, Long> {

    List<FileDB> findBySubjectId(final Long subjectId);

    Optional<FileDB> findById(final Long id);
}
