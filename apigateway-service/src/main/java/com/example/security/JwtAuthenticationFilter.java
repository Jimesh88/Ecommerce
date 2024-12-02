package com.example.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtils jwtUtils; // Assuming you have a JwtUtils class for validating tokens

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Extract the JWT token from the request headers
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix

            try {
                // Validate the token and set the authentication in SecurityContext
                Authentication authentication = jwtUtils.validateToken(token);
                if (authentication != null) {
                    // Set the authentication in the security context
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Handle any exceptions related to invalid tokens (e.g., log the error)
                return Mono.error(new SecurityException("Invalid JWT token"));
            }
        }

        // Continue the filter chain
        return chain.filter(exchange);
    }
}
