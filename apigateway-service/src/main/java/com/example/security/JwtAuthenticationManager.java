package com.example.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationManager(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }



    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();

        if (jwtUtils.validateToken(token)) {
            String username = jwtUtils.getUsernameFromToken(token);

            // Set roles or authorities as needed
            return Mono.just(new UsernamePasswordAuthenticationToken(
                    new User(username, "", List.of(new SimpleGrantedAuthority("ROLE_USER"))),
                    token,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            ));
        }

        return Mono.empty(); // Token is not valid
    }
}

