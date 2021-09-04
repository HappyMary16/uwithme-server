package com.mborodin.uwm.api.enums;

import java.util.List;

import lombok.Getter;

@Getter
public enum SupportedLanguage {

    UK(List.of("uk", "uk-UK")),
    EN(List.of("en", "en-US"));

    private final List<String> representations;

    SupportedLanguage(final List<String> representations) {
        this.representations = representations;
    }
}
