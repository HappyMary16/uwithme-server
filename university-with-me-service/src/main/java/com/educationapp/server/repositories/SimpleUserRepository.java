package com.educationapp.server.repositories;

import com.educationapp.server.models.persistence.SimpleUserDb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleUserRepository extends CrudRepository<SimpleUserDb, String> {

}
