package com.mborodin.uwm.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends UwmException {

    public UserNotFoundException(final String language) {
        super(language);
    }

    public UserNotFoundException(final String language, final String email) {
        super(language, email);
    }

    @Override
    protected String getEnMessage() {
        return "User does not exist";
    }

    @Override
    protected String getEnMessage(final String... params) {
        return String.format("User with e-mail [%s] does not exist", params[0]);
    }

    @Override
    protected String getUkMessage() {
        return "Такого користувача не існує";
    }

    @Override
    protected String getUkMessage(final String... params) {
        return String.format("Користувача з електронною поштою [%s] не існує", params[0]);
    }
}
