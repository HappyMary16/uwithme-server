package com.mborodin.uwm.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class StudCabConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor(final ObjectMapper objectMapper) {
        return new StudCabInterceptor(objectMapper);
    }
}
