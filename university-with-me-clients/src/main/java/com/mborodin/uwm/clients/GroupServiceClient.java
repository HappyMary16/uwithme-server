package com.mborodin.uwm.clients;

import com.mborodin.uwm.api.KeycloakUserApi;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

@Slf4j
@NoArgsConstructor
public class GroupServiceClient {

    @Value("${university-with-me.host:localhost}")
    private String host;
    @Value("${university-with-me.port:${server.port}}")
    private int port;

    private RestTemplate restTemplate;

    public GroupServiceClient(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SneakyThrows
    public KeycloakUserApi getUserById(final String userId) {
//        log.debug("Get KeycloakUser for user with id {}", userId);
//        final String getUserUri = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId;
//
//        final ResponseEntity<KeycloakUserApi> response = restTemplate.getForEntity(getUserUri, KeycloakUserApi.class);
//
//        log.debug("Returned KeycloakUser: {}", response.getBody());
//
//        return response.getBody();
        return null;
    }
}
