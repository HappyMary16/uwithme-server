package com.educationapp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.educationapp.server.repository")
public class EducationAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EducationAppApplication.class, args);
    }

}
