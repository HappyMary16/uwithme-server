package com.educationapp.clients;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.educationapp.api.KeycloakUserApi;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

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
    public KeycloakUserApi getUserById(final String userId) {
        log.debug("Get KeycloakUser for user with id {}", userId);
        final String getUserUri = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId;

        final ResponseEntity<KeycloakUserApi> response = restOperations.getForEntity(getUserUri, KeycloakUserApi.class);

        log.debug("Returned KeycloakUser: {}", response.getBody());

        return response.getBody();
    }

    @SneakyThrows
    public Map<String, KeycloakUserApi> getUsersByIds(final List<String> userIds) {
        log.debug("Get KeycloakUsers for users with ids {}", userIds);

        if (CollectionUtils.isEmpty(userIds)) {

            log.trace("getPlansByIds.X: plans {}", Collections.emptyList());
            return Collections.emptyMap();
        }

        final String joinedArguments = userIds.stream()
                                              .filter(StringUtils::isNotBlank)
                                              .collect(Collectors.joining(","));

        final String getUserUri = keycloakUrl + "/admin/realms/" + realm + "/users?idpUserId=" + joinedArguments;

        final var response = restOperations.getForEntity(getUserUri, List.class);

        log.debug("Returned KeycloakUsers: {}", response.getBody());

        return Map.of();
    }

    @SneakyThrows
    public KeycloakUserApi updateUser(final String userId, final KeycloakUserApi user) {
        log.debug("Update Keycloak User with id {}", userId);

        final String userUri = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId;

        final UserRepresentation userToUpdate =
                Optional.of(restOperations.getForEntity(userUri, UserRepresentation.class))
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
