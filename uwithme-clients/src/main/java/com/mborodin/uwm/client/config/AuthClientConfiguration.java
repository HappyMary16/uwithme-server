package com.mborodin.uwm.client.config;

import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;

/**
 * TODO: add description.
 *
 * @author mariia.borodin (mborodin)
 * @since 1.1
 */
public class AuthClientConfiguration {

    @Bean
    public FeignAccessTokenInterceptor feignTokenInterceptor(final Keycloak keycloak) {
        return new FeignAccessTokenInterceptor(keycloak);
    }
}
