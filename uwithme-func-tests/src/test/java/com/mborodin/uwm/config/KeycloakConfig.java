package com.mborodin.uwm.config;

import static org.keycloak.OAuth2Constants.SCOPE_OPENID;
import static org.keycloak.representations.idm.CredentialRepresentation.PASSWORD;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO: add description.
 *
 * @author mariia.borodin (mborodin)
 * @since 1.1
 */
@Configuration
public class KeycloakConfig {

    @Bean
    public Keycloak keycloak(@Value("${keycloak.resource}") final String clientId,
                             @Value("${keycloak.realm}") final String realm,
                             @Value("${keycloak.auth-server-url}") final String serverUrl,
                             @Value("${oauth.user1.username}") final String username,
                             @Value("${oauth.user1.password}") final String password) {
        return KeycloakBuilder.builder()
                              .clientId(clientId)
                              .serverUrl(serverUrl)
                              .scope(SCOPE_OPENID)
                              .realm(realm)
                              .grantType(PASSWORD)
                              .username(username)
                              .password(password)
                              .build();
    }

    @Bean
    public UsersResource usersResource(final Keycloak keycloak,
                                       @Value("${keycloak.realm}") final String realm) {
        return keycloak.realm(realm).users();
    }
}
