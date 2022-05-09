package com.mborodin.uwm.api;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mborodin.uwm.api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeycloakUserApi {

    private String id;
    private String username;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Set<Role> roles;
}
