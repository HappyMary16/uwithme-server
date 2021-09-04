package com.mborodin.uwm.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class LastAdminCannotBeDeleted extends UwmException {

    public LastAdminCannotBeDeleted(final String language) {
        super(language);
    }

    @Override
    protected String getEnMessage() {
        return "Last admin cannot be deleted.";
    }

    @Override
    protected String getUkMessage() {
        return "Призначте нового адміністратора, щоб видалити акаунт";
    }
}
