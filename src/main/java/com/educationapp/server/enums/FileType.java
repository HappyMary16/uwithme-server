package com.educationapp.server.enums;

import lombok.Getter;

@Getter
public enum FileType {

    LECTURE(1),
    TASK(2);

    private int id;

    FileType(final int id) {
        this.id = id;
    }

    public static Role getById(final int id) {
        if (id > Role.values().length) {
            return null;
        }
        return Role.values()[id - 1];
    }

}
