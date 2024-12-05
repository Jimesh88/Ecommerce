//package com.example.security;
//
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtParser;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Component;
//
//import java.security.SignatureException;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class JwtUtils {
//
//    @Value("${jwt.secret-key}")  // Inject the secret key from application.yml
//    private String secretKey;
//
//    @Value("${jwt.expiration-time}")  // Inject the expiration time from application.yml
//    private long expirationTime;
//
//    // Generate JWT Token for a given username
//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .compact();
//    }
//
//    // Validate the JWT Token
//
//
//    // Validate the JWT token and return Authentication
//    public Authentication validateToken(String token) {
//        try {
//            // Create a JwtParser instance using the parserBuilder method (works with JJWT 0.12.2)
//            JwtParser jwtParser = Jwts.parser()
//                    .setSigningKey(secretKey)  // Use the same secret key for validation
//                    .build();
//
//            // Parse the token and extract claims
//            Claims claims = jwtParser.parseClaimsJws(token).getBody();
//
//            // Extract user info from claims (e.g., subject or username) and create an Authentication object
//            String username = claims.getSubject(); // Usually, the subject is the username in JWT
//            return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
//
//        }  catch (Exception e) {
//            // Catch other exceptions such as expired token, invalid token, etc.
//            // Log or handle appropriately
//            return null; // Invalid token or some other error
//        }
//    }
//
//    // Extract Username from JWT Token
//    public String getUsernameFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//        return claims.getSubject();
//    }
//
//    // Extract Token from Authorization Header
//    public String extractToken(String authorizationHeader) {
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            return authorizationHeader.substring(7);
//        }
//        return null;
//    }
//}
