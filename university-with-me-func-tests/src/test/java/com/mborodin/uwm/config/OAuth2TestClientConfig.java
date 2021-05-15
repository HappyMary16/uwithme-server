package com.mborodin.uwm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class OAuth2TestClientConfig {

    @Value("${oauth.client}")
    private String clientId;
    @Value("${oauth.server}")
    private String serverUri;
    @Value("${oauth.realm}")
    private String realm;
    @Value("${oauth.user1.username}")
    private String user1Username;
    @Value("${oauth.user1.password}")
    private String user1Password;
    @Value("${oauth.user2.username}")
    private String user2Username;
    @Value("${oauth.user2.password}")
    private String user2Password;

    @Bean
    public OAuth2ProtectedResourceDetails oauth2ProtectedResourceDetails() {
        ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
        details.setClientId(clientId);
        details.setAccessTokenUri(serverUri + "/realms/" + realm + "/protocol/openid-connect/token");
        return details;
    }

    @Bean(name = "restTemplateAdmin")
    public RestTemplate restTemplate() {
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(oauth2ProtectedResourceDetails());
        oAuth2RestTemplate.getOAuth2ClientContext().getAccessTokenRequest().set("username", user1Username);
        oAuth2RestTemplate.getOAuth2ClientContext().getAccessTokenRequest().set("password", user1Password);
        return oAuth2RestTemplate;
    }

    @Bean(name = "restTemplateUser2")
    public RestTemplate restTemplateUser2() {
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(oauth2ProtectedResourceDetails());
        oAuth2RestTemplate.getOAuth2ClientContext().getAccessTokenRequest().set("username", user2Username);
        oAuth2RestTemplate.getOAuth2ClientContext().getAccessTokenRequest().set("password", user2Password);
        return oAuth2RestTemplate;
    }
}

