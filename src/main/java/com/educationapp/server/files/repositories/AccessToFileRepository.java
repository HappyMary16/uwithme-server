package com.educationapp.server.files.repositories;

import java.util.List;

import com.educationapp.server.files.models.persistence.AccessToFile;
import org.springframework.data.repository.CrudRepository;

public interface AccessToFileRepository extends CrudRepository<AccessToFile, Long> {

    List<AccessToFile> findAllByStudyGroupId(final Long studyGroupId);
}
