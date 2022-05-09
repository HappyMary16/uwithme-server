package com.mborodin.uwm.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UnknownException extends UwmException {

    public UnknownException(final String language) {
        super(language);
    }

    @Override
    protected String getEnMessage() {
        return "Something went wrong.";
    }

    @Override
    protected String getUkMessage() {
        return "Щось пішло не так.";
    }
}
