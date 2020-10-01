package com.educationapp.server.endpoints;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import javax.validation.Valid;

import com.educationapp.server.models.api.LoginApi;
import com.educationapp.server.models.api.RegisterApi;
import com.educationapp.server.models.api.TokenApi;
import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.security.JwtTokenProvider;
import com.educationapp.server.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid final LoginApi loginApi) {
        final UserApi user = userService.findByUserName(loginApi.getUsername());

        if (user.getPassword().equals(loginApi.getPassword())) {
            user.setAuthToken(tokenProvider.createAuthToken(loginApi.getUsername()));
            user.setRefreshToken(tokenProvider.createRefreshToken(loginApi.getUsername()));

            return ResponseEntity.ok(user);
        }
        return new ResponseEntity<>(UNAUTHORIZED);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> register(@RequestBody final RegisterApi registerApi) {
        if (StringUtils.isEmpty(registerApi.getUsername())) {
            registerApi.setUsername(registerApi.getEmail());
        }

        userService.save(registerApi);

        final String username = registerApi.getUsername();
        final UserApi user = userService.findByUserName(username)
                                        .toBuilder()
                                        .authToken(tokenProvider.createAuthToken(username))
                                        .refreshToken(tokenProvider.createRefreshToken(username))
                                        .build();

        return new ResponseEntity<>(user, OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody final TokenApi refreshTokenApi) {
        final String token = refreshTokenApi.getRefreshToken();

        if (tokenProvider.validateRefreshToken(token)) {
            final String username = tokenProvider.getRefreshTokenUsername(token);
            final TokenApi tokenApi = new TokenApi(tokenProvider.createAuthToken(username),
                                                   tokenProvider.createRefreshToken(username));
            return ResponseEntity.ok(tokenApi);
        }

        return new ResponseEntity<>(UNAUTHORIZED);
    }
}
