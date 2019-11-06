package com.educationapp.server;

import java.net.URI;

import javax.validation.Valid;

import com.educationapp.server.model.domain.User;
import com.educationapp.server.model.mapper.UserMapper;
import com.educationapp.server.model.persistence.UserDB;
import com.educationapp.server.payload.ApiResponse;
import com.educationapp.server.payload.JwtAuthenticationResponse;
import com.educationapp.server.payload.LoginRequest;
import com.educationapp.server.payload.SignUpRequest;
import com.educationapp.server.repository.UserRepository;
import com.educationapp.server.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        UserDB user = userRepository.findByNickname(loginRequest.getUsernameOrEmail());
        if (user.getPassword().equals(user.getPassword())) {
            String jwt = tokenProvider.generateToken(UserMapper.userDbToUser(user));
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByNickname(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                                      HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                                      HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getPhone())) {
            return new ResponseEntity(new ApiResponse(false, "Phone already in use!"),
                                      HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User();
        user.setNickname(signUpRequest.getUsername());
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getName());
        user.setPhone(signUpRequest.getPhone());

//        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserDB result = userRepository.save(UserMapper.userToUserDB(user));

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getNickname()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
