package com.mborodin.uwm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED, reason = "Last admin cannot be deleted. Sorry:(")
public class LastAdminCannotBeDeleted extends RuntimeException {

    public LastAdminCannotBeDeleted() {
        super("Last admin cannot be deleted. Sorry:(");
    }
}
