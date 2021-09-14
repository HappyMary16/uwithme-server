package com.mborodin.uwm.security;

import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.models.persistence.SimpleUserDb;
import com.mborodin.uwm.repositories.SimpleUserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mborodin.uwm.api.enums.Role.ROLE_ADMIN;

@RequiredArgsConstructor
public class CustomKeycloakAuthenticationProvider extends KeycloakAuthenticationProvider {

    private final SimpleUserRepository userRepository;
    private final Set<String> roles = new HashSet<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;

        final var user = userRepository.findById(token.getPrincipal().toString());

        user.map(SimpleUserDb::getRole)
                .map(Role::name)
                .ifPresent(roles::add);

        user.map(SimpleUserDb::getIsAdmin)
                .filter(isAdmin -> isAdmin)
                .ifPresent((isAdmin) -> roles.add(ROLE_ADMIN.name()));

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

