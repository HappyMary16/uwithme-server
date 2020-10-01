package com.educationapp.server.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.educationapp.server.services.UserService;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@AllArgsConstructor
public class UserInitialisationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {
        try {
            final String token = jwtTokenProvider.resolveToken(request);
            final String username = jwtTokenProvider.getUsername(token);

            UserContextHolder.setUser(userService.findByUserName(username));
        } catch (final JwtException | IllegalArgumentException e) {
            log.info("Someone try to refresh token with wrong auth token");
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
