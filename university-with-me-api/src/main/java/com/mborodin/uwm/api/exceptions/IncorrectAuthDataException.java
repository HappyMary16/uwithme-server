package com.mborodin.uwm.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IncorrectAuthDataException extends UwmException {

    public IncorrectAuthDataException(final String language) {
        super(language);
    }

    @Override
    protected String getEnMessage() {
        return "Login on password is incorrect. Please, try again";
    }

    @Override
    protected String getUkMessage() {
        return "Логін або пароль не вірні. Будь ласка, спробуйте ще раз";
    }
}
