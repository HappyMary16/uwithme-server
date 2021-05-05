package com.mborodin.uwm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OAuth2ClientConfig {

    private final OAuth2ProtectedResourceDetails resource;

    public OAuth2ClientConfig(OAuth2ProtectedResourceDetails resource) {
        this.resource = resource;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new OAuth2RestTemplate(resource);
    }
}