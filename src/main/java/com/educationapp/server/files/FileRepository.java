package com.educationapp.server.files;

import java.util.List;

import com.educationapp.server.files.models.persistence.File;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Long> {

    List<File> findBySubjectId(final Long subjectId);
}
