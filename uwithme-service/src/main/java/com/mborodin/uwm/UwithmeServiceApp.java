package com.mborodin.uwm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
public class UwithmeServiceApp extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(UwithmeServiceApp.class, args);
    }
}