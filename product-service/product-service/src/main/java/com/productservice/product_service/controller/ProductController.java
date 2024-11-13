package com.productservice.product_service.controller;

import com.productservice.product_service.model.Product;
import com.productservice.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping
    public Flux<Product> getAllProducts(){
      return  productService.findAllProducts();
    }

    @GetMapping
    @RequestMapping("/{productId}")
    public Mono<Product> findProduct(@PathVariable Long productId){
        return productService.findById(productId);
    }
}
