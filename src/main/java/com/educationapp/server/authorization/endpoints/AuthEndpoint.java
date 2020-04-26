package com.educationapp.server.authorization.endpoints;

import javax.validation.Valid;

import com.educationapp.server.authorization.security.JwtTokenProvider;
import com.educationapp.server.common.api.LoginApi;
import com.educationapp.server.common.api.RefreshTokenApi;
import com.educationapp.server.common.api.RegisterApi;
import com.educationapp.server.common.api.UserApi;
import com.educationapp.server.users.servises.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthEndpoint {

    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginApi loginApi) {
        UserApi user = userService.findByUserName(loginApi.getUsername());

        if (user.getPassword().equals(loginApi.getPassword())) {
            user.setAuthToken(tokenProvider.createToken(loginApi.getUsername()));
            user.setRefreshToken(tokenProvider.createRefreshToken(loginApi.getUsername()));

            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> register(@RequestBody RegisterApi registerApi) {
        userService.save(registerApi);

        UserApi user = userService.findByUserName(registerApi.getUsername());
        user.setAuthToken(tokenProvider.createToken(user.getUsername()));
        user.setRefreshToken(tokenProvider.createRefreshToken(user.getUsername()));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenApi refreshTokenApi) {
        String username = tokenProvider.getUsername(refreshTokenApi.getRefreshToken());

        RefreshTokenApi refreshToken = new RefreshTokenApi(tokenProvider.createToken(username),
                                                           tokenProvider.createRefreshToken(username));

        return ResponseEntity.ok(refreshToken);
    }
}
