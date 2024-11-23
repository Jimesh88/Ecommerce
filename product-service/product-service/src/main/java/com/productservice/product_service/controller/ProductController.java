package com.productservice.product_service.controller;

import com.productservice.product_service.model.Product;
import com.productservice.product_service.repository.ProductRepository;
import com.productservice.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/products")
@Tag(name = "Product API", description = "Operations related to products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieve a list of all products available.")
    @ApiResponse(responseCode = "200", description = "List of products fetched successfully")
    public Flux<Product> getAllProducts(){
      return  productService.findAllProducts();
    }

    @GetMapping
    @RequestMapping("/{productId}")
    @Operation(summary = "Get product by ID", description = "Retrieve a product using its ID.")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public Mono<Product> findProduct(            @Parameter(description = "ID of the product to fetch", required = true)
                                                     @PathVariable Long productId){
        return productService.findById(productId);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update product details", description = "Update the quantity and other details of a product.")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    public Mono<Product> updateProduct(             @Parameter(description = "ID of the product to update", required = true)
                                                         @PathVariable Long productId, @RequestBody Product product) {
        return productService.findById(productId)
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found")))
                .flatMap(existingProduct -> {
                    existingProduct.setQuantity(product.getQuantity());
                    return productRepository.save(existingProduct);
                });
    }
}
