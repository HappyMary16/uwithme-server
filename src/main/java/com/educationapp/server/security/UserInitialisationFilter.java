package com.educationapp.server.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.educationapp.server.models.api.UserApi;
import com.educationapp.server.models.persistence.SimpleUserDb;
import com.educationapp.server.repositories.SimpleUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@AllArgsConstructor
public class UserInitialisationFilter extends OncePerRequestFilter {

    private final SimpleUserRepository simpleUserRepository;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {

        final KeycloakSecurityContext session =
                (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
        final AccessToken token = session.getToken();

        final SimpleUserDb user = simpleUserRepository.findById(token.getSubject()).orElseGet(SimpleUserDb::new);

        UserContextHolder.setUser(UserApi.builder()
                                         .id(token.getSubject())
                                         .firstName(token.getGivenName())
                                         .lastName(token.getMiddleName())
                                         .surname(token.getFamilyName())
                                         .email(token.getEmail())
                                         .role(user.getRole())
                                         .universityId(user.getUniversityId())
                                         .isAdmin(user.getIsAdmin())
                                         .build());

        filterChain.doFilter(request, response);
    }
}
