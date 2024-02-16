// package com.example.userlogin.config;

// import io.jsonwebtoken.Jwts;


// public class JwtUtil {
    
//     private String secret = "secret";

//     public String generateToken(String username) {
//         return Jwts.builder()
//                 .setSubject(username)
//                 .setIssuedAt(new Date(System.currentTimeMillis()))
//                 .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
//                 .signWith(SignatureAlgorithm.HS256, secret)
//                 .compact();
//     }

// }
