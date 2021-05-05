package com.educationapp;

import javax.inject.Inject;

import com.educationapp.clients.KeycloakServiceClient;
import com.educationapp.config.TestConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(classes = {TestConfig.class})
public abstract class AbstractBaseTest extends AbstractTestNGSpringContextTests {

    @Inject
    private KeycloakServiceClient keycloakServiceClient;

    @BeforeClass
    public void setupUsers() {

    }

}
