package com.educationapp.server.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.educationapp.server.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

@AllArgsConstructor
public class UserInitialisationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {
        final String token = jwtTokenProvider.resolveToken(request);
        final String username = jwtTokenProvider.getUsername(token);

        UserContextHolder.setUser(userService.findByUserName(username));

        filterChain.doFilter(request, response);
    }
}
