package com.educationapp.server.common.enums;

import lombok.Getter;

@Getter
public enum Role {

    STUDENT(1),
    TEACHER(2),
    ADMIN(3);

    private int id;

    Role(final int id) {
        this.id = id;
    }

    public static Role getById(final int id) {
        if (id > Role.values().length) {
            return null;
        }
        return Role.values()[id - 1];
    }

}
