package com.mborodin.uwm.api.exceptions.filestorage;

import com.mborodin.uwm.api.exceptions.UwmException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class CouldNotStoreAvatarException extends UwmException {

    public CouldNotStoreAvatarException(final String language) {
        super(language);
    }

    @Override
    protected String getEnMessage() {
        return "Could not store the avatar. Please, try again";
    }

    @Override
    protected String getUkMessage() {
        return "Не вийшло зберегти аватар. Будь ласка, спробуйте знову";
    }
}
