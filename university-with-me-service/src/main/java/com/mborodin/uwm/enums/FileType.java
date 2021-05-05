package com.mborodin.uwm.enums;

import lombok.Getter;

@Getter
public enum FileType {

    LECTURE(1),
    TASK(2);

    private final int id;

    FileType(final int id) {
        this.id = id;
    }

    public static FileType getById(final int id) {
        if (id > FileType.values().length) {
            return null;
        }
        return FileType.values()[id - 1];
    }
}
