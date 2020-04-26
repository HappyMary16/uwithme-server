package com.educationapp.server.users.repositories;

import java.util.List;
import java.util.Optional;

import com.educationapp.server.users.model.persistence.UserDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserDB, Long> {

    Optional<UserDB> findByUsername(final String username);

    List<UserDB> findAllByRoleAndUniversityId(final Integer role, final Long universityId);
}
