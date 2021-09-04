package com.mborodin.uwm.security;

import com.mborodin.uwm.api.KeycloakUserApi;
import com.mborodin.uwm.enums.Role;
import com.mborodin.uwm.models.persistence.SimpleUserDb;
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

    public static KeycloakUserApi getKeycloakUser() {
        return threadLocalScope.get().getKeycloakUser();
    }

    public static String getLanguages() {
        return threadLocalScope.get().getLanguages();
    }

    public static void setUserContext(UserContext user) {
        threadLocalScope.set(user);
    }

    @Value
    @Builder(toBuilder = true)
    public static class UserContext {

        KeycloakUserApi keycloakUser;
        SimpleUserDb userDb;
        String languages;
    }
}
