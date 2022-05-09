package com.mborodin.uwm.repositories;

import java.util.List;

import com.mborodin.uwm.model.persistence.AccessToFileDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessToFileRepository extends CrudRepository<AccessToFileDB, Long> {

    List<AccessToFileDB> findAllByStudyGroupId(final Long studyGroupId);
}
