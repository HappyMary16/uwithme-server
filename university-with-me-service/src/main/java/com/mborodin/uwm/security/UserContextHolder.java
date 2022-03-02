package com.mborodin.uwm.security;

import static com.mborodin.uwm.api.enums.Role.ROLE_ADMIN;

import java.util.Objects;
import java.util.Optional;

import com.mborodin.uwm.api.KeycloakUserApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.api.exceptions.UserNotFoundException;
import com.mborodin.uwm.model.persistence.SimpleUserDb;
import lombok.Builder;
import lombok.Value;

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
        final SimpleUserDb user = getUserDb();
        if (Objects.equals(role, user.getRole())) {
            return true;
        }

        final boolean isAdmin = Optional.ofNullable(user.getIsAdmin())
                                        .orElse(false);

        return Objects.equals(role, ROLE_ADMIN) && isAdmin;
    }

    public static Role getRole() {
        final SimpleUserDb user = getUserDb();
        if (user == null) {
            return null;
        }

        final Role role = user.getRole();
        if (role != null) {
            return role;
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
