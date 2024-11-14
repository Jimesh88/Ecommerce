package com.cartservice.cart_service.service;

import com.cartservice.cart_service.model.Cart;
import com.cartservice.cart_service.repository.CartRepository;
import com.productservice.product_service.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
    public Mono<Cart> addProductToCart(Long cartId, Long productId) {
        // Step 1: Fetch product details from Product Service
        Mono<Product> productMono = webClientBuilder.baseUrl(productServiceBaseUrl)
                .build()
                .get()
                .uri("/{productId}", productId)
                .retrieve()
                .bodyToMono(Product.class);

        // Step 2: Find the cart by cartId, or return error if not found
        return productMono.flatMap(product ->
                cartRepository.findById(cartId)
                        .switchIfEmpty(Mono.error(new RuntimeException("Cart not found")))
                        .flatMap(cart -> {
                            // Step 3: Update the cart with the new productId and update totalPrice
                            cart.setproductId(productId);  // Set the product ID in the cart
                            cart.setTotalPrice(product.getPrice());  // Set the price of the single product

                            // Step 4: Save the updated cart
                            return cartRepository.save(cart);
                        })
        );
    }


    @Override
    public Mono<Cart> removeProductFromCart(Long cartId, Long productId) {
        // Step 1: Fetch the cart from the repository by cartId
        return cartRepository.findById(cartId)
                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found")))
                .flatMap(cart -> {
                    // Step 2: Check if the cart has the given productId, if so, remove it
                    if (cart.getproductId().equals(productId)) {
                        // Step 3: Fetch the product to get its price
                        return webClientBuilder.baseUrl(productServiceBaseUrl)
                                .build()
                                .get()
                                .uri("/{productId}", productId)
                                .retrieve()
                                .bodyToMono(Product.class)
                                .flatMap(product -> {
                                    // Step 4: Set the productId to null and totalPrice to 0 (since no product in the cart)
                                    cart.setproductId(null);
                                    cart.setTotalPrice(0.0);

                                    // Step 5: Save the updated cart
                                    return cartRepository.save(cart);
                                });
                    } else {
                        return Mono.error(new RuntimeException("Product not found in cart"));
                    }
                });
    }


    @Override
    public Mono<Cart> viewCartItems(Long cartId) {
        // Step 1: Find the cart by cartId
        return cartRepository.findById(cartId)
                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found")))
                .flatMap(cart -> {
                    // Step 2: Check if the cart has a productId
                    Long productId = cart.getproductId();

                    // If there's no product in the cart, we return the cart with a total price of 0
                    if (productId == null) {
                        cart.setTotalPrice(0.0);  // Set total price to 0 if no product
                        return Mono.just(cart);
                    }

                    // Step 3: Fetch the product details from the Product Service
                    return webClientBuilder.baseUrl(productServiceBaseUrl)
                            .build()
                            .get()
                            .uri("/{productId}", productId)
                            .retrieve()
                            .bodyToMono(Product.class)
                            .flatMap(product -> {
                                // Step 4: Set the product details (price) into the cart
                                cart.setTotalPrice(product.getPrice());
                                return Mono.just(cart); // Return the cart with the product details and total price
                            });
                });
    }


}
