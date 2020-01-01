package com.educationapp.server.files.repositories;

import java.util.List;

import com.educationapp.server.files.models.persistence.AccessToFileDB;
import org.springframework.data.repository.CrudRepository;

public interface AccessToFileRepository extends CrudRepository<AccessToFileDB, Long> {

    List<AccessToFileDB> findAllByStudyGroupId(final Long studyGroupId);
}
