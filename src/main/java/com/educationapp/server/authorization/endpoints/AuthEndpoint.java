package com.educationapp.server.authorization.endpoints;

import java.util.Collections;

import javax.validation.Valid;

import com.educationapp.server.authorization.security.JwtTokenProvider;
import com.educationapp.server.common.api.LoginApi;
import com.educationapp.server.common.api.RegisterApi;
import com.educationapp.server.common.api.UserApi;
import com.educationapp.server.users.repositories.UserRepository;
import com.educationapp.server.users.servises.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<UserApi> authenticateUser(@RequestBody @Valid LoginApi loginApi) {
        UserApi user = userService.findByUserName(loginApi.getUsername());

        if (user.getPassword().equals(loginApi.getPassword())) {
            user.setToken(tokenProvider.createToken(loginApi.getUsername(),
                                                    Collections.singletonList(user.getRole().toString())));

            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserApi> register(@RequestBody RegisterApi registerApi) {
        userService.save(registerApi);

        LoginApi loginApi = new LoginApi(registerApi.getUsername(), registerApi.getPassword());
        UserApi user = userService.findByUserName(loginApi.getUsername());
        user.setToken(tokenProvider.createToken(loginApi.getUsername(),
                                                Collections.singletonList(user.getRole().toString())));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
