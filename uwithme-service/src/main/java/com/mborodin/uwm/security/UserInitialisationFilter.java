package com.mborodin.uwm.security;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mborodin.uwm.model.persistence.UserDb;
import com.mborodin.uwm.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.UserRepresentation;
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

        final UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(userId);
        userRepresentation.setFirstName(token.getGivenName());
        userRepresentation.setLastName(token.getFamilyName());
        userRepresentation.setEmail(token.getEmail());
        userRepresentation.setRealmRoles(Optional.ofNullable(token)
                                                 .map(AccessToken::getRealmAccess)
                                                 .map(AccessToken.Access::getRoles)
                                                 .map(ArrayList::new)
                                                 .orElseGet(ArrayList::new));

        UserContextHolder.setUserContext(UserContextHolder.UserContext.builder()
                                                                      .keycloakUser(userRepresentation)
                                                                      .userDb(userDb)
                                                                      .languages(request.getHeader(ACCEPT_LANGUAGE))
                                                                      .build());

        filterChain.doFilter(request, response);
    }
}
