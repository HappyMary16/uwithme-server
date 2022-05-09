package com.mborodin.uwm.api.enums;

import java.util.List;

import lombok.Getter;

@Getter
public enum SupportedLanguage {

    UA(List.of("ua", "uk-UA")),
    EN(List.of("en", "en-US"));

    private final List<String> representations;

    SupportedLanguage(final List<String> representations) {
        this.representations = representations;
    }
}
