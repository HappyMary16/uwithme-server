package com.educationapp.server.clients;

import com.educationapp.server.models.KeycloakUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import java.util.Optional;

@Slf4j
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
        log.debug("Get KeycloakUser for user with id {}", userId);
        final String getUserUri = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId;

        final ResponseEntity<KeycloakUser> response = restOperations.getForEntity(getUserUri, KeycloakUser.class);

        log.debug("Returned KeycloakUser: {}", response.getBody());

        return response.getBody();
    }

    @SneakyThrows
    public KeycloakUser updateUser(final String userId, final KeycloakUser user) {
        log.debug("Update Keycloak User with id {}", userId);

        final String userUri = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId;

        final UserRepresentation userToUpdate = Optional.of(restOperations.getForEntity(userUri, UserRepresentation.class))
                .map(ResponseEntity::getBody)
                .stream()
                .peek(u -> {
                    u.setFirstName(user.getFirstName());
                    u.setLastName(user.getLastName());
                    u.setEmail(user.getEmail());
                })
                .findFirst()
                .orElse(null);


        try {
            restOperations.put(userUri, userToUpdate);
        } catch (RestClientException e) {
            log.error(e.getMessage());
        }

        return getUserById(userId);
    }
}
