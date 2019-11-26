package com.educationapp.server.model.api;

public class LoginForm {

    private String username;
    private String password;

    public LoginForm(final String username, final String password) {
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
