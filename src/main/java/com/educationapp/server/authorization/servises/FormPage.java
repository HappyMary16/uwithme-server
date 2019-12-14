package com.educationapp.server.authorization.servises;

import static com.codeborne.selenide.Selenide.$;

import java.io.File;
import java.io.IOException;

public class FormPage {

    public FormPage setSeries(final String series) {
        $("#documentSeries").hover().setValue(series);
        return this;
    }

    public FormPage setNumber(final String number) {
        $("#documentNumber").hover().setValue(number);
        return this;
    }

    public FormPage setLastName(final String name) {
        $("#lastName").hover().setValue(name);
        return this;
    }

    public FormPage setFirstName(final String surname) {
        $("#firstName").hover().setValue(surname);
        return this;
    }

    public FormPage setMiddleName(final String patronymic) {
        $("#middleName").hover().setValue(patronymic);
        return this;
    }

    public FormPage setCaptcha(final String captcha) {
        $("#captcha").hover().setValue(captcha);
        return this;
    }

    public File getCaptcha() throws IOException {
        return $("#imgCaptcha").screenshot();
    }

    public void submit() {
        $("div.buttons-panel").$("button").click();
    }
}
