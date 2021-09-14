package com.mborodin.uwm.clients;

import com.mborodin.uwm.api.KeycloakUserApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.config.ClientAuthConfig;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "keycloakServiceClient",
        url = "${keycloak.auth-server-url}/admin/realms/${keycloak.realm}",
        configuration = ClientAuthConfig.class)
public interface KeycloakServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}")
    KeycloakUserApi getUser(@PathVariable("userId") String userId);

    @RequestMapping(method = RequestMethod.GET, value = "/roles/{role}/users")
    List<KeycloakUserApi> getUsersByRole(@PathVariable("role") Role role, @RequestParam("max") int max);

    @RequestMapping(method = RequestMethod.GET, value = "/roles/{role}")
    RoleRepresentation getRole(@PathVariable("role") String role);

    @RequestMapping(method = RequestMethod.POST, value = "/users/{userId}/role-mappings/realm")
    RoleRepresentation assignRole(@PathVariable("userId") String userId,
                                  List<RoleRepresentation> roleRepresentations);

    default void assignRole(final String userId, final String role) {
        final RoleRepresentation roleRepresentation = getRole(role);
        assignRole(userId, List.of(roleRepresentation));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/users/{userId}/role-mappings/realm")
    RoleRepresentation unAssignRole(@PathVariable("userId") String userId,
                                  List<RoleRepresentation> roleRepresentations);

    default void unAssignRole(final String userId, final Role role) {
        final RoleRepresentation roleRepresentation = getRole(role.name());
        unAssignRole(userId, List.of(roleRepresentation));
    }
}
