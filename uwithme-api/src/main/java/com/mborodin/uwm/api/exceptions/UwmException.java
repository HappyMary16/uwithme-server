package com.mborodin.uwm.api.exceptions;

import static com.mborodin.uwm.api.enums.SupportedLanguage.UA;

import java.util.Arrays;
import java.util.Objects;

import com.mborodin.uwm.api.enums.SupportedLanguage;

public abstract class UwmException extends RuntimeException {

    private String message;

    protected UwmException(final String language, final String... params) {
        super();

        setMessage(language, params);
    }

    @Override
    public final String getMessage() {
        return message;
    }

    protected String getEnMessage() {
        return "";
    }

    protected String getUkMessage() {
        return "";
    }

    protected String getEnMessage(final String... params) {
        return getEnMessage();
    }

    protected String getUkMessage(final String... params) {
        return getUkMessage();
    }

    private void setMessage(final String language, final String... params) {
        switch (getSupportedLanguage(language)) {
            case EN:
                if (params.length == 0) {
                    message = getEnMessage();
                } else {
                    message = getEnMessage(params);
                }
                return;
            case UA:
                if (params.length == 0) {
                    message = getUkMessage();
                } else {
                    message = getUkMessage(params);
                }
        }
    }

    private SupportedLanguage getSupportedLanguage(String languages) {
        if (Objects.isNull(languages)) {
            return UA;
        }

        //TODO: it may not work, verify before using
        return Arrays.stream(languages.split(","))
                     .map(String::trim)
                     .map(l -> l.split(";")[0])
                     .map(this::getSupportedByLanguage)
                     .filter(Objects::nonNull)
                     .findFirst()
                     .orElse(UA);
    }

    private SupportedLanguage getSupportedByLanguage(String language) {
        return Arrays.stream(SupportedLanguage.values())
                     .filter(supportedLanguage -> supportedLanguage.getRepresentations()
                                                                   .contains(language))
                     .findFirst()
                     .orElse(null);
    }
}
