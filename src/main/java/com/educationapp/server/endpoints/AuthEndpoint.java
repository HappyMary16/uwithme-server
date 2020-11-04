package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.OK;

import com.educationapp.server.models.api.RegisterApi;
import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthEndpoint {

    private final UserService userService;

    @GetMapping("/signIn")
    public ResponseEntity<?> authenticateUser() {
        return new ResponseEntity<>(userService.getUserApi(), OK);
    }

    @PostMapping("/signUp")
    public UserApi register(@RequestBody final RegisterApi registerApi) {
        userService.save(registerApi);
        return userService.getUserApi();
    }
}
