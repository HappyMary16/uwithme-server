package com.mborodin.uwm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.api.structure.GroupApi;
import com.mborodin.uwm.client.client.UwmClient;
import com.mborodin.uwm.client.config.AuthClientConfiguration;
import com.mborodin.uwm.config.KeycloakConfig;
import com.mborodin.uwm.tests.GroupTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestApplication.class})
public class AbstractBaseTest {

    @Autowired
    protected UwmClient uwmClient;

    @BeforeEach
    public void setupUsers() {
        uwmClient.register(RegisterApi.builder()
                                      .role(Role.ROLE_ADMIN)
                                      .universityName("TEST")
                                      .build());
    }
}
