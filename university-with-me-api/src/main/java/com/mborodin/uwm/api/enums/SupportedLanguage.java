package com.mborodin.uwm.api.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum SupportedLanguage {

    UA(List.of("ua", "ua-UA")),
    EN(List.of("en", "en-US"));

    private final List<String> representations;

    SupportedLanguage(final List<String> representations) {
        this.representations = representations;
    }
}
