package com.educationapp.server.repository;

import java.util.Optional;

import com.educationapp.server.model.persistence.UserDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserDB, Long> {

    Optional<UserDB> findByUsername(final String username);

    Optional<UserDB> findByUsernameAndEmail(final String username, final String email);

    boolean existsByUsername(final String username);

    boolean existsByEmail(String email);

}
