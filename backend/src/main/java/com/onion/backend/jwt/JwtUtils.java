package com.onion.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    private final Key key;
    private final String secretKey;
    private final Long expire;

    public JwtUtils(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expire}") Long expire){
        this.secretKey = secretKey;
        this.expire = expire;
        this.key = Keys.hmacShaKeyFor(this.secretKey.getBytes());
    }

    /**
     * Generates a JWT token with the specified username.
     *
     * @param username the username to include in the token
     * @return the generated token
     */
    public String generateToken(String username){
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expire))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Validates a given JWT token.
     *
     * @param token the token to validate
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Invalid token
            return false;
        }
    }

    /**
     * Extracts the username (subject) from a given JWT token.
     *
     * @param token the token to extract the username from
     * @return the username (subject) contained in the token
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

}
