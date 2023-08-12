package com.mborodin.uwm.services;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.mborodin.uwm.api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

/**
 * TODO: add description.
 *
 * @author mariia.borodin (mborodin)
 * @since 1.1
 */
@Slf4j
@AllArgsConstructor
@Service
public class KeycloakUserService {

    private final UsersResource usersResource;

    public UserRepresentation getUser(final String userId) {
        return usersResource.get(userId).toRepresentation();
    }

    public Set<Role> getUserRoles(final String userId) {
        return usersResource.get(userId)
                            .roles()
                            .realmLevel()
                            .listEffective()
                            .stream()
                            .map(RoleRepresentation::getName)
                            .map(this::valueOfRoleSuppressExceptions)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
    }

    public void deleteUser(final String userId) {
        usersResource.delete(userId);
    }

    private Role valueOfRoleSuppressExceptions(final String role) {
        try {
            return Role.valueOf(role);
        } catch (Exception e) {
            return null;
        }
    }
}
