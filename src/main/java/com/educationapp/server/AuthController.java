package com.educationapp.server;

import java.util.Optional;

import com.educationapp.server.model.api.LoginForm;
import com.educationapp.server.model.domain.User;
import com.educationapp.server.model.persistence.UserDB;
import com.educationapp.server.repository.UserRepository;
import com.educationapp.server.services.UserService;
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
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDB> authenticateUser(@RequestBody LoginForm loginForm) {

        Optional<UserDB> user = userRepository.findByUsername(loginForm.getUsername());
        if (user.isPresent() && user.get().getPassword().equals(loginForm.getPassword())) {
            return new ResponseEntity(user, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDB> register(@RequestBody User loginForm) {
        User user = userService.save(loginForm);
        return new ResponseEntity(user, HttpStatus.OK);
    }

}
