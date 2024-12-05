package com.productservice.product_service;


import com.productservice.product_service.model.Product;

import com.productservice.product_service.repository.ProductRepository;

import com.productservice.product_service.service.ProductService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import reactor.core.publisher.Flux;

import reactor.core.publisher.Mono;

import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@SpringBootTest

public class ProductServiceTest {

    @Autowired

    private ProductService productService;

    @MockBean

    private ProductRepository productRepository;

    @Test

    void testGetAllProducts() {

        Product product1 = new Product();

        product1.setId(1L);

        product1.setName("Product A");

        product1.setPrice(20.0);

        product1.setQuantity(100);

        Product product2 = new Product();

        product2.setId(2L);

        product2.setName("Product B");

        product2.setPrice(10.0);

        product2.setQuantity(50);

        when(productRepository.findAll()).thenReturn(Flux.just(product1, product2));

        Flux<Product> productFlux = productService.findAllProducts().distinctUntilChanged();

        StepVerifier.create(productFlux)

                .expectNext(product1)

                .expectNext(product2)

                .verifyComplete();

    }

    @Test

    void testFindProductById() {

        Long productId = 1L;

        Product product = new Product();

        product.setId(productId);

        product.setName("Product A");

        product.setPrice(20.0);

        product.setQuantity(100);

        when(productRepository.findById(productId)).thenReturn(Mono.just(product));

        Mono<Product> productMono = productService.findById(productId);

        StepVerifier.create(productMono)

                .expectNext(product)

                .verifyComplete();

    }

}

