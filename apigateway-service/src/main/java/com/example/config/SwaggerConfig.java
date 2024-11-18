package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder().title("API Gateway - Centralized Swagger")
                        .description("Centralized Swagger for Cart, Product, and Gateway")
                        .version("1.0")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.apigateway_service"))  // Gateway package
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket cartServiceApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Cart Service")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cartservice.cart_service"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("Cart Service API")
                        .description("Swagger documentation for Cart Service")
                        .version("1.0")
                        .build());
    }

    @Bean
    public Docket productServiceApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Product Service")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.productservice.product_service"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("Product Service API")
                        .description("Swagger documentation for Product Service")
                        .version("1.0")
                        .build());
    }
}