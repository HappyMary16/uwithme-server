package com.educationapp.server.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@AllArgsConstructor
public class UserInitialisationFilter extends OncePerRequestFilter {

    private final UserService userService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {

        final KeycloakSecurityContext session =
                (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        final AccessToken token = session.getToken();

        final String userId = token.getSubject();
        final UserApi user = Optional.ofNullable(userService.findById(userId))
                                     .orElseGet(UserApi::new);

        UserContextHolder.setUser(user.toBuilder()
                                      .id(userId)
                                      .firstName(token.getGivenName())
                                      .lastName(token.getMiddleName())
                                      .surname(token.getFamilyName())
                                      .email(token.getEmail())
                                      .build());

        filterChain.doFilter(request, response);
    }
}
