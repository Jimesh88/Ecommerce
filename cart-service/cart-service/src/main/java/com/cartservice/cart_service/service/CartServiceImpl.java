package com.cartservice.cart_service.service;

import com.cartservice.cart_service.model.Cart;
import com.cartservice.cart_service.repository.CartRepository;
import com.productservice.product_service.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private final WebClient.Builder webClientBuilder;
    private final String productServiceBaseUrl = "http://localhost:8080/products";

    @Autowired
    CartRepository cartRepository;

    public CartServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Cart> addProductToCart(Long cartid, Long productId) {
        Mono<Product> productMono = webClientBuilder.baseUrl(productServiceBaseUrl)
                .build()
                .get()
                .uri("/{productId}", productId)
                .retrieve()
                .bodyToMono(Product.class);

        return productMono.flatMap(product -> cartRepository.findById(cartid)
                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found")))
                .flatMap(cart -> {
                    cart.addProduct(product.getId());
                    cart.setTotalPrice(cart.getTotalPrice() + product.getPrice());
                    return cartRepository.save(cart);
                }));
    }

    @Override
    public Mono<Cart> removeProductFromCart(Long cartid, Long productId) {
        return cartRepository.findById(cartid)
                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found")))
                .flatMap(cart -> {
                    cart.removeProduct(productId);
                    // Assuming you have a way to get the product price
                    WebClient webClient = webClientBuilder.baseUrl(productServiceBaseUrl).build();
                    return webClient.get()
                            .uri("/{productId}", productId)
                            .retrieve()
                            .bodyToMono(Product.class)
                            .flatMap(product -> {
                                cart.setTotalPrice(cart.getTotalPrice() - product.getPrice());
                                return cartRepository.save(cart);
                            });
                });
    }

    @Override
    public Mono<Cart> viewCartItems(Long cartid) {
        return cartRepository.findById(cartid)
                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found")))
                .flatMap(cart -> {
                    WebClient webClient = webClientBuilder.baseUrl(productServiceBaseUrl).build();
                    List<Mono<Product>> productMonos = cart.getProductIds().stream()
                            .map(productId -> webClient.get()
                                    .uri("/{productId}", productId)
                                    .retrieve()
                                    .bodyToMono(Product.class))
                            .collect(Collectors.toList());

                    return Mono.zip(productMonos, products -> {
                        List<Product> productList = new ArrayList<>();
                        for (Object obj : products) {
                            productList.add((Product) obj);
                        }
                        double totalPrice = productList.stream().mapToDouble(Product::getPrice).sum();
                        cart.setTotalPrice(totalPrice);
                        return cart;
                    });
                });
    }
}
