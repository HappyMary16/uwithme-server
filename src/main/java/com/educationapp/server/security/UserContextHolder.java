package com.educationapp.server.security;

import com.educationapp.server.models.api.UserApi;

public class UserContextHolder {

    private static final InheritableThreadLocal<UserApi> threadLocalScope = new InheritableThreadLocal<>();

    public static UserApi getUser() {
        return threadLocalScope.get();
    }

    public static void setUser(UserApi user) {
        threadLocalScope.set(user);
    }
}
