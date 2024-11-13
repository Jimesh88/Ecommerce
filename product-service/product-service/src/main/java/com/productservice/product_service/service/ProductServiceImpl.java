package com.productservice.product_service.service;

import com.productservice.product_service.model.Product;
import com.productservice.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Override
    public Flux<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> findById(Long productId) {
        return productRepository.findById(productId)
                .doOnNext(product -> System.out.println("Product found"))
                .doOnError(error -> System.err.println("Error finding product"+ error.getMessage()));
    }


}
