package com.educationapp.server.security;

import com.educationapp.server.exception.ResourceNotFoundException;
import com.educationapp.server.model.mapper.UserMapper;
import com.educationapp.server.model.persistence.UserDB;
import com.educationapp.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        UserDB user = userRepository.findByNicknameAndEmail(usernameOrEmail, usernameOrEmail)
                                    .orElseThrow(() ->
                                                         new UsernameNotFoundException(
                                                                 "User not found with username or email : " +
                                                                         usernameOrEmail)
                                                );

        return UserPrincipal.create(UserMapper.userDbToUser(user));
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        UserDB user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
                                                             );

        return UserPrincipal.create(UserMapper.userDbToUser(user));
    }
}