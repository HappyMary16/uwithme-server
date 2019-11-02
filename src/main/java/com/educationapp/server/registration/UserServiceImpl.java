package com.educationapp.server.registration;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.educationapp.server.model.domain.User;
import com.educationapp.server.model.mapper.UserMapper;
import com.educationapp.server.model.persistence.UserDB;
import com.educationapp.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(UserMapper.userToUserDB(user));
    }

    @Override
    public User findByUsername(String username) {
        return UserMapper.userDbToUser(userRepository.findByNickname(username));
    }

    @Override
    public User findById(Long id) {
        return UserMapper.userDbToUser(userRepository.findById(id).orElse(new UserDB()));
    }

    @Override
    public List<User> findAll() {
        return ((List<UserDB>) userRepository.findAll()).stream()
                                                        .map(UserMapper::userDbToUser)
                                                        .collect(Collectors.toList());
    }
}
