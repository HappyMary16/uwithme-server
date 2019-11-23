package com.educationapp.server;

import java.util.Optional;

import com.educationapp.server.model.persistence.UserDB;
import com.educationapp.server.repository.UserRepository;
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

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginForm loginForm) {

        Optional<UserDB> user = userRepository.findByNickname(loginForm.username);
        if (user.get().getPassword().equals(loginForm.password)) {
            System.out.println("success");
        }
        System.out.println("error");
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    public static class LoginForm {

        private String username;
        private String password;

        public LoginForm(final String username, final String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
