package com.mborodin.uwm.security;

import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mborodin.uwm.api.KeycloakUserApi;
import com.mborodin.uwm.models.persistence.SimpleUserDb;
import com.mborodin.uwm.repositories.SimpleUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.web.filter.OncePerRequestFilter;

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
                                                                      .languages(request.getHeader(ACCEPT_LANGUAGE))
                                                                      .build());

        filterChain.doFilter(request, response);
    }
}
