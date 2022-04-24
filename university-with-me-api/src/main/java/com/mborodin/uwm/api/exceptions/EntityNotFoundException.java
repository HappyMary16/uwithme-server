package com.mborodin.uwm.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends UwmException {

    public EntityNotFoundException(final String language) {
        super(language);
    }

    @Override
    protected String getEnMessage() {
        return "Entity was not found.";
    }

    @Override
    protected String getUkMessage() {
        return "Об'єкт не вдалося знайти.";
    }
}
