package com.educationapp.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EmptyFieldsException extends RuntimeException {

    public EmptyFieldsException() {
        super("Please, fill all fields");
    }
}
