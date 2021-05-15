package com.mborodin.uwm.clients;

import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.UserApi;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
@NoArgsConstructor
public class AuthServiceClient {

    @Value("${university-with-me.host:localhost}")
    private String host;
    @Value("${university-with-me.port:${server.port}}")
    private int port;

    private RestTemplate restTemplate;

    public AuthServiceClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SneakyThrows
    public UserApi register(final RegisterApi registerApi) {
        log.debug("Register User: {}", registerApi);

        final String getUserUri = "https://" + host + ":" + port + "/api/auth/signUp";

        final ResponseEntity<UserApi> response = restTemplate.postForEntity(getUserUri, registerApi, UserApi.class);

        log.debug("Created User: {}", response.getBody());

        return response.getBody();
    }
}
