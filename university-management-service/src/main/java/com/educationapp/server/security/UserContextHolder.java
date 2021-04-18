package com.educationapp.server.security;

import com.educationapp.server.enums.Role;
import com.educationapp.server.models.KeycloakUser;
import com.educationapp.server.models.persistence.SimpleUserDb;
import lombok.Builder;
import lombok.Value;

public class UserContextHolder {

    private static final InheritableThreadLocal<UserContext> threadLocalScope = new InheritableThreadLocal<>();

    public static Long getUniversityId() {
        if (getUserDb() == null) {
            return null;
        }

        return getUserDb().getUniversityId();
    }

    public static String getId() {
        return getKeycloakUser().getId();
    }

    public static Role getRole() {
        final SimpleUserDb user = getUserDb();
        if (user == null) {
            return null;
        }

        final Integer role = user.getRole();
        if (role != null) {
            return Role.getById(role);
        }
        return null;
    }

    public static Long getGroupId() {
        final SimpleUserDb user = getUserDb();
        if (user == null) {
            return null;
        }

        return user.getGroupId();
    }

    public static SimpleUserDb getUserDb() {
        return threadLocalScope.get().getUserDb();
    }

    public static KeycloakUser getKeycloakUser() {
        return threadLocalScope.get().getKeycloakUser();
    }

    public static void setUserContext(UserContext user) {
        threadLocalScope.set(user);
    }

    @Value
    @Builder(toBuilder = true)
    public static class UserContext {

        KeycloakUser keycloakUser;
        SimpleUserDb userDb;
    }
}
