package com.educationapp.server.authorization.security;

import java.util.Date;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import com.educationapp.server.users.servises.UserService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String refreshTokenSecretKey = "veryLongSecretKeyveryLongSecretKeyveryLongSecretKeyveryLongSecretKeyveryLongSecretKeyveryLongSecretKey";
    private final SecretKey authTokenSecretKey;
    private final long authTokenExpirationTime = 3600_000; //1h
    private final long refreshTokenExpirationTime = 3600_000 * 7; //1 week

    private final UserService userDetailsService;

    @SneakyThrows
    public JwtTokenProvider(final UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
        authTokenSecretKey = KeyGenerator.getInstance("HmacSHA256").generateKey();
    }

    public String createAuthToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + authTokenExpirationTime);

        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(now)
                   .setExpiration(expiration)
                   .signWith(authTokenSecretKey)
                   .compact();
    }

    public String createRefreshToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshTokenExpirationTime);

        return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(new Date())
                   .setExpiration(expiration)
                   .signWith(SignatureAlgorithm.HS512, refreshTokenSecretKey)
                   .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser()
                   .setSigningKey(authTokenSecretKey)
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    public String getRefreshTokenUsername(String token) {
        return Jwts.parser()
                   .setSigningKey(refreshTokenSecretKey)
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
            Jwts.parser().setSigningKey(authTokenSecretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(refreshTokenSecretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}