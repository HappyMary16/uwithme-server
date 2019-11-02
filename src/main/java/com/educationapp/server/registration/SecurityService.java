package com.educationapp.server.registration;

public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String nickname, String password);
}