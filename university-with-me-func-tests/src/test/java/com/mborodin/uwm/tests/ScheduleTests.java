package com.mborodin.uwm.tests;

import com.mborodin.uwm.AbstractBaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ScheduleTests extends AbstractBaseTest {

    @Test
    void testEmailGenerator() {

        String email = "";
        System.out.println(email);

        Assert.assertNotNull(email);
        Assert.assertEquals(email, "");
    }
}
