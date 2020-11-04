package com.educationapp.server.security;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.educationapp.server.enums.Role;
import com.educationapp.server.models.persistence.SimpleUserDb;
import com.educationapp.server.repositories.SimpleUserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
public class CustomKeycloakAuthenticationProvider extends KeycloakAuthenticationProvider {

    private final SimpleUserRepository simpleUserRepository;
    private final Set<String> roles = new HashSet<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;

        final SimpleUserDb user = simpleUserRepository.findById(token.getPrincipal().toString()).orElse(null);
        if (Objects.nonNull(user)) {
            final String role = Role.getById(user.getRole()).name();
            roles.add(role);
        }

        Set<GrantedAuthority> keycloakAuthorities = addKeycloakRoles(token);
        Set<GrantedAuthority> grantedAuthorities = addUserAuthorities(keycloakAuthorities);

        return new KeycloakAuthenticationToken(token.getAccount(), token.isInteractive(), grantedAuthorities);
    }

    protected Set<GrantedAuthority> addUserAuthorities(Set<GrantedAuthority> authorities) {
        Set<GrantedAuthority> result = new HashSet<>(authorities);
        roles.forEach(role -> result.add(new SimpleGrantedAuthority(role)));
        return result;
    }

    protected Set<GrantedAuthority> addKeycloakRoles(KeycloakAuthenticationToken token) {
        return token.getAccount()
                    .getRoles()
                    .stream()
                    .map(KeycloakRole::new)
                    .collect(Collectors.toSet());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return KeycloakAuthenticationToken.class.isAssignableFrom(aClass);
    }
}

