package com.mborodin.uwm.repositories;

import com.mborodin.uwm.models.persistence.SimpleUserDb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleUserRepository extends CrudRepository<SimpleUserDb, String> {

}
