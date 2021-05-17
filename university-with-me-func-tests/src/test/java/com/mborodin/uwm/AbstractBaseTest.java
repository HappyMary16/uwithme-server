package com.mborodin.uwm;

import javax.inject.Inject;
import javax.inject.Named;

import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.clients.AuthServiceClient;
import com.mborodin.uwm.config.ClientConfig;
import com.mborodin.uwm.config.OAuth2TestClientConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test
@TestPropertySource("classpath:application.properties")
@ContextConfiguration(classes = {OAuth2TestClientConfig.class,
        ClientFactory.class,
        ClientConfig.class})
public abstract class AbstractBaseTest extends AbstractTestNGSpringContextTests {

    @Inject
    @Named("authServiceClientAdmin")
    private AuthServiceClient authServiceClientAdmin;

    @BeforeClass
    public void setupUsers() {
        authServiceClientAdmin.register(RegisterApi.builder()
                                                   .role(3)
                                                   .universityName("TEST")
                                                   .build());
    }
}
