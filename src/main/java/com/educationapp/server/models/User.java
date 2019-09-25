package com.educationapp.server.models;

import lombok.Data;

import java.util.Random;

@Data
public class User {

    private final Long id;
    private String name;
    private String lastName;

    public User() {
        this.id = new Random().nextLong();
    }
}
