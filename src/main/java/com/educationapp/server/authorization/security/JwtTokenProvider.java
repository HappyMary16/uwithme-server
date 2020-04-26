package com.educationapp.server.authorization.security;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import com.educationapp.server.users.servises.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private SecretKey secretKey;
    private long validityInMilliseconds = 3600000 * 24; // 1h

    @Autowired
    private UserService userDetailsService;

    private KeyGenerator keyGenerator;

    @PostConstruct
    protected void init() {
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            secretKey = null;
            e.printStackTrace();
        }
    }

    public String createToken(String username, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(now)
                   .setExpiration(validity)
                   .signWith(secretKey)
                   .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return claims.getBody().getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}