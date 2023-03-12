package com.mborodin.uwm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * TODO: add description.
 *
 * @author mariia.borodin (mborodin)
 * @since 1.1
 */
@SpringBootApplication
@EnableFeignClients
public class TestApplication {

    public static void main(String... args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
