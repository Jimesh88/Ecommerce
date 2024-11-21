package com.productservice.product_service.controller;

import com.productservice.product_service.model.Product;
import com.productservice.product_service.repository.ProductRepository;
import com.productservice.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @GetMapping
    public Flux<Product> getAllProducts(){
      return  productService.findAllProducts();
    }

    @GetMapping
    @RequestMapping("/{productId}")
    public Mono<Product> findProduct(@PathVariable Long productId){
        return productService.findById(productId);
    }

    @PutMapping("/{productId}")
    public Mono<Product> updateProduct(@PathVariable Long productId, @RequestBody Product product) {
        return productService.findById(productId)
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")))
                .flatMap(existingProduct -> {
                    existingProduct.setQuantity(product.getQuantity());
                    return productRepository.save(existingProduct);
                });
    }
}
