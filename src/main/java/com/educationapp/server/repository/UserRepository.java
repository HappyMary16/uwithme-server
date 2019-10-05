package com.educationapp.server.repository;

import com.educationapp.server.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByFirstNameAndLastName(final String firstName, final String lastName);
}
