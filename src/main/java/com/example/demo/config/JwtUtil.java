package com.example.demo.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private String secret = "your_secret_key"; // Ensure this is at least 256-bit for HS512
    private int expirationTime = 1000 * 60 * 60; // 1 hour
    private Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key) // Using the simplified method as recommended
                .compact();
    }

    
    
}




