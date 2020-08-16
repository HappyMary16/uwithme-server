package com.educationapp.server.authorization.endpoints;

import javax.validation.Valid;

import com.educationapp.server.authorization.security.JwtTokenProvider;
import com.educationapp.server.common.api.LoginApi;
import com.educationapp.server.common.api.RegisterApi;
import com.educationapp.server.common.api.TokenApi;
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
            user.setAuthToken(tokenProvider.createAuthToken(loginApi.getUsername()));
            user.setRefreshToken(tokenProvider.createRefreshToken(loginApi.getUsername()));

            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserApi> register(@RequestBody RegisterApi registerApi) {
        userService.save(registerApi);

        String username;
        if (registerApi.getUsername() == null) {
            username = registerApi.getEmail();
        } else {
            username = registerApi.getUsername();
        }
        UserApi user = userService.findByUserName(username);
        user.setAuthToken(tokenProvider.createAuthToken(username));
        user.setRefreshToken(tokenProvider.createRefreshToken(username));

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody TokenApi refreshTokenApi) {
        final String token = refreshTokenApi.getRefreshToken();

        if (tokenProvider.validateRefreshToken(token)) {
            final String username = tokenProvider.getRefreshTokenUsername(token);
            final TokenApi tokenApi = new TokenApi(tokenProvider.createAuthToken(username),
                                                   tokenProvider.createRefreshToken(username));
            return ResponseEntity.ok(tokenApi);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("");
    }
}
