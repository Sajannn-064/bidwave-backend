package com.bidwave.bidwave_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

// @Component — marks this as a Spring Bean
// lives in config because it supports SecurityConfig
@Component
public class JwtUtil {

    // secret key loaded from application.properties
    // never hardcode secrets in source code
    @Value("${jwt.secret}")
    private String secretKey;

    // token validity — 24 hours in milliseconds
    private static final long EXPIRATION_TIME = 86400000;

    // generates a signing key from the secret string
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // generates a JWT token for a given username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)           // payload — who this token is for
                .setIssuedAt(new Date())         // when token was created
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // expiry
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // sign with secret key
                .compact();                      // build and return token string
    }

    // extracts all claims (payload data) from a token
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // verify signature using secret key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // extracts username from token payload
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // checks if token is expired
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // validates token — checks signature and expiry
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }
}