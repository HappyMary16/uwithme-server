package com.educationapp.server.authorization.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.educationapp.server.users.servises.UserService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String secretKey = "veryLongSecretKeyveryLongSecretKeyveryLongSecretKeyveryLongSecretKeyveryLongSecretKeyveryLongSecretKey";
    private final long validityInMilliseconds = 3600_000; //1h

    private final UserService userDetailsService;

    public JwtTokenProvider(final UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String createToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(now)
                   .setExpiration(validity)
                   .signWith(SignatureAlgorithm.HS512, secretKey)
                   .compact();
    }

    public String createRefreshToken(String username) {
        return Jwts.builder()
                   .setIssuedAt(new Date())
                   .setSubject(username)
                   .setIssuer("Refresh")
                   .signWith(SignatureAlgorithm.HS512, secretKey)
                   .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser()
                   .setSigningKey(secretKey)
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String prefix = "Bearer ";
        if (bearerToken != null && bearerToken.startsWith(prefix)) {
            return bearerToken.substring(prefix.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}