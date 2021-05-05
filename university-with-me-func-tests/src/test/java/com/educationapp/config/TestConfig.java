package com.educationapp.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
@ComponentScan("com.educationapp.clients")
public class TestConfig {

    @Bean
    RestOperations restOperations() {
        return new RestTemplate();
    }
}
