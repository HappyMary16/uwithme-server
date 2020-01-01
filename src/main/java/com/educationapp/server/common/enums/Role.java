package com.educationapp.server.common.enums;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN(0),
    STUDENT(1),
    TEACHER(2);

    private int id;

    Role(final int id) {
        this.id = id;
    }

    public static Role getById(final int id) {
        if (id >= Role.values().length) {
            return null;
        }
        return Role.values()[id];
    }

}
