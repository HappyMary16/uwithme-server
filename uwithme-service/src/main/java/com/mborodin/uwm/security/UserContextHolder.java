package com.mborodin.uwm.security;

import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.api.exceptions.UserNotFoundException;
import com.mborodin.uwm.model.persistence.UserDb;
import lombok.Builder;
import lombok.Value;
import org.keycloak.representations.idm.UserRepresentation;

public class UserContextHolder {

    private static final InheritableThreadLocal<UserContext> threadLocalScope = new InheritableThreadLocal<>();

    public static long getUniversityId() {
        if (getUserDb() == null) {
            throw new UserNotFoundException(getLanguages(), getKeycloakUser().getEmail());
        }

        return getUserDb().getUniversityId();
    }

    public static String getId() {
        return getKeycloakUser().getId();
    }

    public static boolean hasRole(final Role role) {
        return getKeycloakUser().getRealmRoles().contains(role.name());
    }

    public static Long getGroupId() {
        final UserDb user = getUserDb();
        if (user == null) {
            return null;
        }

        return user.getGroupId();
    }

    public static UserDb getUserDb() {
        return threadLocalScope.get().getUserDb();
    }

    public static UserRepresentation getKeycloakUser() {
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

        UserRepresentation keycloakUser;
        UserDb userDb;
        String languages;
    }
}
