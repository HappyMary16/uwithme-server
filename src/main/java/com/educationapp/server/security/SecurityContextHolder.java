package com.educationapp.server.security;

import com.educationapp.server.models.api.UserApi;

public class SecurityContextHolder {

    private static final InheritableThreadLocal<UserApi> threadLocalScope = new InheritableThreadLocal<>();

    public final static UserApi getLoggedUser() {
        return threadLocalScope.get();
    }

    public final static void setLoggedUser(UserApi user) {
        threadLocalScope.set(user);
    }
}
