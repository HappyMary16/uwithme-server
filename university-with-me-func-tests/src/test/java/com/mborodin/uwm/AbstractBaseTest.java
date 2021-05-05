package com.mborodin.uwm;

import javax.inject.Inject;

import com.mborodin.uwm.clients.KeycloakServiceClient;
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
