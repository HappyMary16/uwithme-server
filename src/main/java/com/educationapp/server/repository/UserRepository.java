package com.educationapp.server.repository;

import com.educationapp.server.model.persistence.UserDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserDB, Long> {

    UserDB findByNickname(final String nickname);
}
