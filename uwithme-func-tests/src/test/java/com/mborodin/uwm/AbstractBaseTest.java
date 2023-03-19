package com.mborodin.uwm;

import java.util.Objects;

import com.mborodin.uwm.api.RegisterApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.client.client.UwmClient;
import com.mborodin.uwm.client.client.core.TenantClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestApplication.class})
public class AbstractBaseTest {

    public static String TENANT_NAME = "TEST";

    private static boolean isTenantCreated = false;

    @Autowired
    protected TenantClient tenantClient;
    @Autowired
    protected UwmClient uwmClient;

    @BeforeEach
    public void setupUsers() {
        if (!isTenantCreated) {
            try {
                isTenantCreated = Objects.nonNull(tenantClient.getUniversity());
            } catch (final Throwable ignored) {
            }
        }

        if (!isTenantCreated) {
            uwmClient.register(RegisterApi.builder()
                                          .role(Role.ROLE_ADMIN)
                                          .universityName(TENANT_NAME)
                                          .build());
            isTenantCreated = true;
        }
    }

    @AfterEach
    public void cleanUp() {
//        uwmClient.deleteAccount();
    }
}
