package com.educationapp.server.authorization.models;

import javax.validation.constraints.NotNull;

public class LoginApi {

    @NotNull
    private String username;

    @NotNull
    private String password;

    public LoginApi(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
