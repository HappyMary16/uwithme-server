package com.mborodin.uwm.api;

import java.util.List;

import com.mborodin.uwm.api.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

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
    private List<Role> roles;
}
