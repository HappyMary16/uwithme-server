package com.mborodin.uwm.tests;

import javax.inject.Inject;

import com.mborodin.uwm.AbstractBaseTest;
import com.mborodin.uwm.clients.GroupServiceClient;
import org.junit.Before;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ScheduleTests extends AbstractBaseTest {

    @Inject
    private GroupServiceClient groupServiceClient;

    @Before
    void setup() {

    }

    @Test
    void testEmailGenerator() {

        String email = "";
        System.out.println(email);

        Assert.assertNotNull(email);
        Assert.assertEquals(email, "");
    }
}
