package com.educationapp.server.services;

import com.educationapp.server.model.domain.User;
import com.educationapp.server.model.mapper.UserMapper;
import com.educationapp.server.model.persistence.UserDB;
import com.educationapp.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    public User save(User user) {
        UserDB created = userRepository.save(UserMapper.userToUserDB(user));
        return UserMapper.userDbToUser(created);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserMapper.userDbToUser(userRepository.findByUsername(username)
                                                     .orElseThrow(
                                                             () -> new UsernameNotFoundException("Username: " + username + " not found")));
    }
}
