package com.mborodin.uwm.client.config;

import static org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor.AUTHORIZATION;
import static org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor.BEARER;

import java.util.List;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;

/**
 * Interceptor for adding Bearer access token to request headers.
 *
 * @author mariia.borodin (mborodin)
 * @since 1.1
 */
@AllArgsConstructor
public class FeignAccessTokenInterceptor implements RequestInterceptor {

    private final Keycloak keycloak;

    public void apply(RequestTemplate template) {
        final String token = keycloak.tokenManager().getAccessTokenString();
        template.header(AUTHORIZATION, String.join(" ", List.of(BEARER, token)));
    }
}