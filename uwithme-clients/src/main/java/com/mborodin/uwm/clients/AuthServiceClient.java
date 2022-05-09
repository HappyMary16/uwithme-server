package com.mborodin.uwm.clients;

import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.UserApi;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
public class AuthServiceClient {

    private String host;
    private int port;
    private RestTemplate restTemplate;

    @SneakyThrows
    public UserApi register(final RegisterApi registerApi) {
        log.debug("Register User: {}", registerApi);

        final String getUserUri = "http://" + host + ":" + port + "/api/auth/signUp";

        final ResponseEntity<UserApi> response = restTemplate.postForEntity(getUserUri, registerApi, UserApi.class);

        log.debug("Created User: {}", response.getBody());

        return response.getBody();
    }
}
