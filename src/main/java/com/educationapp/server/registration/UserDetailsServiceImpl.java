package com.educationapp.server.registration;

import java.util.HashSet;
import java.util.Set;

import com.educationapp.server.model.persistence.UserDB;
import com.educationapp.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String nickname) {
        UserDB user = userRepository.findByNickname(nickname);
        if (user == null) {
            throw new UsernameNotFoundException(nickname);
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        grantedAuthorities.add(new SimpleGrantedAuthority(user.toString()));

        return new org.springframework.security.core.userdetails.User(user.getNickname(), user.getPassword(),
                                                                      grantedAuthorities);
    }
}
