package com.cartservice.cart_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced  // This ensures that WebClient uses Eureka for service discovery
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}