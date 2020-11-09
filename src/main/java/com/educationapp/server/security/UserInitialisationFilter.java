package com.educationapp.server.security;

import static com.educationapp.server.services.UserService.mapUserDbToUserApi;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.models.persistence.UserDb;
import com.educationapp.server.repositories.UserRepository;
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
            UserContextHolder.setUser(null);
            filterChain.doFilter(request, response);
            return;
        }

        final AccessToken token = session.getToken();

        final String userId = token.getSubject();
        final UserDb user = userRepository.findById(userId).orElseGet(UserDb::new);
        final UserApi userApi = mapUserDbToUserApi(user);

        UserContextHolder.setUser(userApi.toBuilder()
                                         .id(userId)
                                         .firstName(token.getGivenName())
                                         .lastName(token.getMiddleName())
                                         .surname(token.getFamilyName())
                                         .email(token.getEmail())
                                         .build());

        filterChain.doFilter(request, response);
    }
}
