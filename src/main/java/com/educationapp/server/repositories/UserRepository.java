package com.educationapp.server.repositories;

import java.util.Optional;

import com.educationapp.server.models.persistence.UserDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserDB, String> {

    Optional<UserDB> findById(final String username);
}
