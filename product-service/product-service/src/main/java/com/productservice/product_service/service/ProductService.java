package com.productservice.product_service.service;

import com.productservice.product_service.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ProductService {
    public Flux<Product> findAllProducts();
    public Mono<Product> findById(Long productId);
}
