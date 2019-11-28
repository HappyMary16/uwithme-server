package com.educationapp.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.educationapp.server.config.JwtTokenProvider;
import com.educationapp.server.model.api.LoginForm;
import com.educationapp.server.model.domain.User;
import com.educationapp.server.model.persistence.UserDB;
import com.educationapp.server.repository.UserRepository;
import com.educationapp.server.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/auth")
@CrossOrigin("*")
//@Validated
public class AuthController {

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomUserDetailsService userService;

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginForm loginForm) {

        Optional<UserDB> user = userRepository.findByUsername(loginForm.getUsername());
        if (user.isPresent() && user.get().getPassword().equals(loginForm.getPassword())) {
            System.out.println("OK");
            return new ResponseEntity(tokenProvider.createToken(user.get().getUsername(),
                    Collections.singletonList("ADMIN")), HttpStatus.OK);
        }
        System.out.println("BED");
        return new ResponseEntity(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDB> register(@RequestBody User loginForm) {
        User user = userService.save(loginForm);
        return new ResponseEntity(user, HttpStatus.OK);
    }

}
