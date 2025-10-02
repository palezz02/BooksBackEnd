package com.betacom.books.be.configurations;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;
import java.util.Base64;
import javax.crypto.SecretKey;

public class JwtUtils {
    private static final String SECRET = "secretKeyDaSostituireConUnaRandomica"; // min 256 bit
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24h

    public static String generateToken(String username, List<String> roles,Integer userId) {
        return Jwts.builder()
            .setSubject(username)
            .claim("roles", roles)
            .claim("userId", userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(KEY)
            .compact();
    }

    public static Jws<Claims> validateToken(String token) throws JwtException {
        return Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
    }
}