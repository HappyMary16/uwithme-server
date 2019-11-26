package com.educationapp.server.services;

import com.educationapp.server.model.domain.User;
import com.educationapp.server.model.mapper.UserMapper;
import com.educationapp.server.model.persistence.UserDB;
import com.educationapp.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User save(User user) {
        UserDB created = userRepository.save(UserMapper.userToUserDB(user));
        return UserMapper.userDbToUser(created);
    }
}
