package com.mborodin.uwm.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class OAuth2ClientConfig {

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        var authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                                                     .clientCredentials()
                                                     .refreshToken()
                                                     .build();

        var authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                                                         oAuth2AuthorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    public Keycloak keycloak(@Value("${keycloak.resource}") String clientId,
                             @Value("${keycloak.credentials.secret}") String clientSecret,
                             @Value("${keycloak.realm}") String realm,
                             @Value("${keycloak.auth-server-url}") String serverUrl,
                             @Value("${spring.security.oauth2.client.registration.keycloak.scope}") String scope,
                             @Value("${spring.security.oauth2.client.registration.keycloak.authorization-grant-type}") String grantType) {
        return KeycloakBuilder.builder()
                              .clientId(clientId)
                              .clientSecret(clientSecret)
                              .serverUrl(serverUrl)
                              .scope(scope)
                              .realm(realm)
                              .grantType(grantType)
                              .build();
    }

    @Bean
    public UsersResource usersResource(final Keycloak keycloak,
                                       @Value("${keycloak.realm}") String realm) {
        return keycloak.realm(realm).users();
    }

    @Bean
    public RolesResource rolesResource(final Keycloak keycloak,
                                       @Value("${keycloak.realm}") String realm) {
        return keycloak.realm(realm).roles();
    }
}