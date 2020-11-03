package com.educationapp.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;


@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class EducationApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(EducationApp.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(EducationApp.class);
    }
}