package com.productservice.product_service.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
public class R2dbcConfig{

    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactoryBuilder.withUrl("r2dbc:h2:file:///./testdb;DB_CLOSE_DELAY=-1")
                .username("sa")
                .password("")
                .build();
    }
}