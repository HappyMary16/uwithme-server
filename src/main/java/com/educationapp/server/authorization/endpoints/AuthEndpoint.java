package com.educationapp.server.authorization.endpoints;

import static com.educationapp.server.enums.Role.ADMIN;

import java.util.Collections;
import java.util.Optional;

import javax.validation.Valid;

import com.educationapp.server.authorization.models.RegisterApi;
import com.educationapp.server.authorization.security.JwtTokenProvider;
import com.educationapp.server.authorization.models.LoginApi;
import com.educationapp.server.users.model.domain.User;
import com.educationapp.server.users.model.persistence.UserDB;
import com.educationapp.server.users.repositories.UserRepository;
import com.educationapp.server.authorization.servises.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthEndpoint {

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomUserDetailsService userService;

    @PostMapping("/signIn")
    public ResponseEntity<String> authenticateUser(@RequestBody @Valid LoginApi loginApi) {

        Optional<UserDB> user = userRepository.findByUsername(loginApi.getUsername());
        if (user.isPresent() && user.get().getPassword().equals(loginApi.getPassword())) {
            return new ResponseEntity<>(tokenProvider.createToken(user.get().getUsername(),
                                                                  Collections.singletonList(ADMIN.name())),
                                        HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterApi registerApi) {
        User user = userService.save(registerApi);
        return new ResponseEntity<>(tokenProvider.createToken(user.getUsername(),
                                                              Collections.singletonList(ADMIN.name())),
                                    HttpStatus.OK);
    }
}
