package com.mborodin.uwm.config;

import javax.inject.Inject;

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

    @Inject
    private ApplicationContext context;

    @Bean
    public GroupServiceClient groupServiceClient() {
        return new GroupServiceClient(context.getBean("restTemplateAdmin", RestTemplate.class));
    }

    @Bean(name = "authServiceClientAdmin")
    public AuthServiceClient authServiceClientAdmin() {
        return new AuthServiceClient(context.getBean("restTemplateAdmin", RestTemplate.class));
    }
}
