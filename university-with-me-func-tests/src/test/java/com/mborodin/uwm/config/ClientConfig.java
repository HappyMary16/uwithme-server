package com.mborodin.uwm.config;

import com.mborodin.uwm.ClientFactory;
import com.mborodin.uwm.clients.AuthServiceClient;
import com.mborodin.uwm.clients.GroupServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
@RequiredArgsConstructor
public class ClientConfig {

    private final ApplicationContext context;
    private final ClientFactory clientFactory;

    @Bean
    public GroupServiceClient groupServiceClient() {
        return clientFactory.getGroupServiceClient(context.getBean("restTemplateAdmin", RestTemplate.class));
    }

    @Bean(name = "authServiceClientAdmin")
    public AuthServiceClient authServiceClientAdmin() {
        return clientFactory.getAuthServiceClient(context.getBean("restTemplateAdmin", RestTemplate.class));
    }
}