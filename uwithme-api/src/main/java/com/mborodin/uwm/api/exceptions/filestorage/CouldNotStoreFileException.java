package com.mborodin.uwm.api.exceptions.filestorage;

import com.mborodin.uwm.api.exceptions.UwmException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class CouldNotStoreFileException extends UwmException {

    public CouldNotStoreFileException(final String language, final String fileName) {
        super(language, fileName);
    }

    @Override
    protected String getEnMessage(final String... params) {
        return String.format("Could not store file %s. Please, try again", params[0]);
    }

    @Override
    protected String getUkMessage(final String... params) {
        return String.format("Не вийшло зберегти файл %s. Будь ласка, спробуйте знову", params[0]);
    }
}
