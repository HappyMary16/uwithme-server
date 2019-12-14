package com.educationapp.server;

import static com.codeborne.selenide.Selenide.page;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.codeborne.selenide.Selenide;
import com.educationapp.server.authorization.servises.FormPage;

public class Test {

    @org.junit.Test
    public void test() {
        Selenide.open("https://info.edbo.gov.ua/student-tickets/");

        String captcha = getCaptcha();

        page(FormPage.class)
                .setSeries("series")
                .setNumber("number")
                .setLastName("name")
                .setFirstName("surename")
                .setMiddleName("patronymic")
                .setCaptcha(captcha)
                .submit();

        Selenide.close();
    }

    public String getCaptcha() {
        try {
            File captchaBufferedImage = page(FormPage.class).getCaptcha();
            FileOutputStream fileOutputStream = new FileOutputStream(captchaBufferedImage);
            Files.copy(new FileInputStream(captchaBufferedImage),
                       Paths.get("D:\\Programming\\Projects\\EducationAppServer\\"));


            return parseCaptcha(captchaBufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String parseCaptcha(File bufferedInputStream) {
       return "captcha";
    }
}
