package com.mborodin.uwm.security;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mborodin.uwm.api.KeycloakUserApi;
import com.mborodin.uwm.api.enums.Role;
import com.mborodin.uwm.model.persistence.UserDb;
import com.mborodin.uwm.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@AllArgsConstructor
public class UserInitialisationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {
        final KeycloakSecurityContext session =
                (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

        if (session == null) {
            UserContextHolder.setUserContext(null);
            filterChain.doFilter(request, response);
            return;
        }

        final AccessToken token = session.getToken();

        final String userId = token.getSubject();
        final UserDb userDb = userRepository.findById(userId).orElse(null);

        final List<Role> roles = token.getRealmAccess()
                                      .getRoles()
                                      .stream()
                                      .map(this::getRoleSuppressExceptions)
                                      .filter(Objects::nonNull)
                                      .collect(Collectors.toList());

        final KeycloakUserApi keycloakUser = KeycloakUserApi.builder()
                                                            .id(userId)
                                                            .firstName(token.getGivenName())
                                                            .middleName(token.getMiddleName())
                                                            .lastName(token.getFamilyName())
                                                            .email(token.getEmail())
                                                            .roles(roles)
                                                            .build();

        UserContextHolder.setUserContext(UserContextHolder.UserContext.builder()
                                                                      .keycloakUser(keycloakUser)
                                                                      .userDb(userDb)
                                                                      .languages(request.getHeader(ACCEPT_LANGUAGE))
                                                                      .build());

        filterChain.doFilter(request, response);
    }

    private Role getRoleSuppressExceptions(final String role) {
        try {
            return Role.valueOf(role);
        } catch (Exception e) {
            return null;
        }
    }
}
