package com.mborodin.uwm.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class EmptyFieldsException extends UwmException {

    public EmptyFieldsException(final String language) {
        super(language);
    }

    @Override
    protected String getEnMessage() {
        return "Please, fill all required fields";
    }

    @Override
    protected String getUkMessage() {
        return "Будь ласка, заповніть всі обов'язкові поля";
    }
}
