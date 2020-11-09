package com.educationapp.server.clients;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.educationapp.server.models.KeycloakUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
@RequiredArgsConstructor
public class KeycloakServiceClient {

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    private final RestOperations restOperations;

    @SneakyThrows
    public KeycloakUser getUserById(final String userId) {
        final String getUserUri = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId;
        final ObjectMapper objectMapper = new ObjectMapper().enable(ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                                                            .disable(FAIL_ON_UNKNOWN_PROPERTIES,
                                                                     FAIL_ON_IGNORED_PROPERTIES);

        final ResponseEntity<String> response = restOperations.getForEntity(getUserUri, String.class);
        return objectMapper.readValue(response.getBody(), KeycloakUser.class);
    }
}
