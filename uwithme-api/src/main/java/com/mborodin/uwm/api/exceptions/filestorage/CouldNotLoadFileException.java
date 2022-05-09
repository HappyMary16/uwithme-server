package com.mborodin.uwm.api.exceptions.filestorage;

import static java.lang.String.format;

import com.mborodin.uwm.api.exceptions.UwmException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class CouldNotLoadFileException extends UwmException {

    public CouldNotLoadFileException(final String language, final String fileName) {
        super(language, fileName);
    }

    @Override
    protected String getEnMessage(final String... params) {
        return format("Could not load the file %s", params[0]);
    }

    @Override
    protected String getUkMessage(final String... params) {
        return String.format("Не вийшло завантажити файл %s", params[0]);
    }
}
