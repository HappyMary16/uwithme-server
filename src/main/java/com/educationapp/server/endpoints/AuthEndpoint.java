package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.Objects;

import com.educationapp.server.models.api.RegisterApi;
import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.security.UserContextHolder;
import com.educationapp.server.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthEndpoint {

    private final UserService userService;

    @GetMapping("/signIn")
    public ResponseEntity<?> authenticateUser() {
        final UserApi user = UserContextHolder.getUser();
        if (Objects.isNull(user.getRole())) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(user, OK);
    }

    @PostMapping("/signUp")
    public UserApi register(@RequestBody final RegisterApi registerApi) {
        userService.save(registerApi);

        final UserApi user = UserContextHolder.getUser();
        final UserApi createdUser = userService.findById(user.getId());

        createdUser.toBuilder()
                   .firstName(user.getFirstName())
                   .lastName(user.getLastName())
                   .surname(user.getSurname())
                   .email(user.getEmail())
                   .build();

        return createdUser;
    }
}
