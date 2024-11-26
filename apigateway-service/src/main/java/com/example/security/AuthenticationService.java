package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    // Hardcoded username and password for authentication
    private static final String HARDCODED_USERNAME = "user";
    private static final String HARDCODED_PASSWORD = "password";

    // Authenticate the user and return the JWT token
    public String authenticateUser(String username, String password) {
        if (HARDCODED_USERNAME.equals(username) && HARDCODED_PASSWORD.equals(password)) {
            return jwtUtils.generateToken(username);  // Generate and return JWT token
        }
        throw new RuntimeException("Invalid username or password");
    }
}
