package com.mborodin.uwm.repositories;

import java.util.List;
import java.util.Optional;

import com.mborodin.uwm.models.persistence.FileDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<FileDB, Long> {

    List<FileDB> findAllBySubjectId(final Long subjectId);

    Optional<FileDB> findById(final Long id);
}
