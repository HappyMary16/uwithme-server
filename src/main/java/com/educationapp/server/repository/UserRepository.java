package com.educationapp.server.repository;

import java.util.Optional;

import com.educationapp.server.model.persistence.UserDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserDB, Long> {

    Optional<UserDB> findByNickname(final String nickname);

    Optional<UserDB> findByNicknameAndEmail(final String nickname, final String email);

    boolean existsByNickname(final String nickname);

    boolean existsByEmail(String email);

}
