package com.educationapp.server.exception;

import static java.lang.String.format;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFound extends RuntimeException {

    public UserNotFound(final String email) {
        super(format("User with e-mail [%s] does not exist", email));
    }
}
