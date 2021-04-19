package com.educationapp.server.security;

import com.educationapp.api.KeycloakUserApi;
import com.educationapp.server.models.persistence.SimpleUserDb;
import com.educationapp.server.repositories.SimpleUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class UserInitialisationFilter extends OncePerRequestFilter {

    private final SimpleUserRepository userRepository;

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
        final SimpleUserDb userDb = userRepository.findById(userId).orElse(null);
        final KeycloakUserApi keycloakUser = KeycloakUserApi.builder()
                                                      .id(userId)
                                                      .firstName(token.getGivenName())
                                                      .middleName(token.getMiddleName())
                                                      .lastName(token.getFamilyName())
                                                      .email(token.getEmail())
                                                      .build();

        UserContextHolder.setUserContext(UserContextHolder.UserContext.builder()
                                                                      .keycloakUser(keycloakUser)
                                                                      .userDb(userDb)
                                                                      .build());

        filterChain.doFilter(request, response);
    }
}
