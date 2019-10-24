package com.educationapp.server.repository;

import com.educationapp.server.models.UserDB;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserDB, Long> {

    List<UserDB> findByFirstNameAndLastName(final String firstName, final String lastName);
}
