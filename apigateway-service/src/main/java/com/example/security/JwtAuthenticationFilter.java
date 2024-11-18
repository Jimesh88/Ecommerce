package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Extract JWT from Authorization header
        String token = extractToken(exchange);

        if (token == null || !validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }

        // Continue the request chain if the JWT is valid
        return chain.filter(exchange);
    }

    private String extractToken(ServerWebExchange exchange) {
        // Extract token from Authorization header (Bearer <token>)
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            // Use JwtParserBuilder to build a parser and parse the JWT token
            Claims claims = Jwts.parser()  // Use parserBuilder instead of parser()
                    .setSigningKey(jwtSecret)      // Set the signing key (your secret)
                    .build()                       // Build the parser
                    .parseClaimsJws(token)         // Parse the JWT token
                    .getBody();                    // Extract claims

            // Optionally: You can validate the claims further if needed, e.g., check expiration, subject, etc.
            return true; // Token is valid
        } catch (Exception e) {
            // Token is invalid or expired
            return false;
        }
    }
}
