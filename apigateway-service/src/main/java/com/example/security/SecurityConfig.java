package com.example.security;

import com.example.security.JwtAuthenticationFilter;
import com.example.security.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUtils jwtUtils;

    public SecurityConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    // Configure the AuthenticationManager to authenticate users
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Configure HttpSecurity to disable CSRF and enable JWT authentication
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Disable CSRF protection
                .authorizeRequests()
                .antMatchers("/auth/login").permitAll()  // Allow login requests without authentication
                .antMatchers("/cart/**", "/products/**").authenticated()  // Protect /cart and /products paths
                .anyRequest().permitAll()  // Allow other paths without authentication
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtUtils)); // Add JWT filter
    }
}
