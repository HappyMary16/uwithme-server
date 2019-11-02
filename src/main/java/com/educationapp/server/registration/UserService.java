package com.educationapp.server.registration;

import java.util.List;

import com.educationapp.server.model.domain.User;

public interface UserService {

    void save(User user);

    User findByUsername(String username);

    User findById(Long id);

    List<User> findAll();
}
