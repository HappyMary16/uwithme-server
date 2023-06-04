package com.mborodin.uwm.client.config;

import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;

public class AuthClientConfiguration {

    @Bean
    public FeignAccessTokenInterceptor feignTokenInterceptor(final Keycloak keycloak) {
        return new FeignAccessTokenInterceptor(keycloak);
    }
}
