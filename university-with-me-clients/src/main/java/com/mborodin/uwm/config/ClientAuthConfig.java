package com.mborodin.uwm.config;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@RequiredArgsConstructor
@Configuration
public class ClientAuthConfig {

    private final ClientCredentialsResourceDetails resourceDetails;

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resourceDetails);
    }

}
