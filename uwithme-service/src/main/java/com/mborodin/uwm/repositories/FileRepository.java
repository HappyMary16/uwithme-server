package com.mborodin.uwm.repositories;

import java.util.List;

import com.mborodin.uwm.model.persistence.FileDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<FileDB, Long> {

    List<FileDB> findAllByOwner(final String owner);

    List<FileDB> findAllByPathIsNull();

    List<FileDB> findAllByOwnerIsNull();
}
