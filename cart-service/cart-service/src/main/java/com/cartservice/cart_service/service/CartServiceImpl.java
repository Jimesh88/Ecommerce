package com.cartservice.cart_service.service;

import com.cartservice.cart_service.dto.CartResponse;
import com.cartservice.cart_service.model.Cart;
import com.cartservice.cart_service.model.CartItem;
import com.cartservice.cart_service.repository.CartRepository;
import com.cartservice.cart_service.repository.CartItemRepository;
import com.productservice.product_service.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final String productServiceBaseUrl = "http://product-service/products";

    @Override
    public Mono<CartResponse> addProductToCart(Long cartId, Long productId, int quantity) {
        return cartRepository.findById(cartId)
                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found")))
                .flatMap(cart -> {
                    // Fetch the product from product-service using WebClient
                    return webClientBuilder.baseUrl(productServiceBaseUrl)
                            .build()
                            .get()
                            .uri("/{productId}", productId)
                            .retrieve()
                            .bodyToMono(Product.class)
                            .switchIfEmpty(Mono.error(new RuntimeException("Product not found")))
                            .flatMap(product -> {
                                // Check if there is enough stock
                                if (product.getQuantity() < quantity) {
                                    return Mono.error(new RuntimeException("Not enough stock for product: " + productId));
                                }

                                // Calculate the price to add to the cart's total price
                                double priceToAdd = product.getPrice() * quantity;

                                // Check if the product already exists in the cart
                                return cartItemRepository.findByCartId(cartId)
                                        .filter(item -> item.getProductId().equals(productId))
                                        .next() // Get the first match
                                        .flatMap(existingItem -> {
                                            if (existingItem != null) {
                                                // If the product is already in the cart, just update the quantity
                                                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                                                return cartItemRepository.save(existingItem)
                                                        .flatMap(savedItem -> {
                                                            // Update the total price of the cart
                                                            cart.setTotalPrice(cart.getTotalPrice() + priceToAdd);
                                                            return updateProductStockAndSaveCart(productId, quantity, cart);
                                                        });
                                            } else {
                                                // Otherwise, create a new CartItem and add it to the cart
                                                CartItem newItem = new CartItem();
                                                newItem.setCartId(cartId);
                                                newItem.setProductId(productId);
                                                newItem.setQuantity(quantity);

                                                return cartItemRepository.save(newItem)
                                                        .flatMap(savedItem -> {
                                                            // Update the total price of the cart
                                                            cart.setTotalPrice(cart.getTotalPrice() + priceToAdd);
                                                            return updateProductStockAndSaveCart(productId, quantity, cart);
                                                        });
                                            }
                                        });
                            });
                })
                .flatMap(cart -> cartItemRepository.findByCartId(cartId)
                        .collectList()
                        .map(items -> {
                            CartResponse response = new CartResponse();
                            response.setCart(cart);
                            response.setItems(items);
                            return response;
                        }));
    }


    @Override
    public Mono<CartResponse> removeProductFromCart(Long cartId, Long productId, int quantity) {
        return cartRepository.findById(cartId)
                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found")))
                .flatMap(cart -> {
                    // Find the CartItem by cartId and productId
                    return cartItemRepository.findByCartId(cartId)
                            .filter(item -> item.getProductId().equals(productId))  // Filter CartItems by productId
                            .next()  // Get the first (or only) matching item
                            .switchIfEmpty(Mono.error(new RuntimeException("Product not found in cart")))
                            .flatMap(cartItem -> {
                                // Fetch the product from product-service to get the price
                                return webClientBuilder.baseUrl(productServiceBaseUrl)
                                        .build()
                                        .get()
                                        .uri("/{productId}", productId)
                                        .retrieve()
                                        .bodyToMono(Product.class)
                                        .flatMap(product -> {
                                            // Calculate the price to subtract from the cart's total price
                                            double priceToSubtract = product.getPrice() * quantity;

                                            // Decrease quantity or remove product from cart
                                            if (cartItem.getQuantity() > quantity) {
                                                cartItem.setQuantity(cartItem.getQuantity() - quantity);
                                                // Save the updated CartItem
                                                return cartItemRepository.save(cartItem)
                                                        .flatMap(savedItem -> {
                                                            // Update the total price of the cart
                                                            cart.setTotalPrice(cart.getTotalPrice() - priceToSubtract);
                                                            return updateProductStockAndSaveCart(productId, -quantity, cart);
                                                        });
                                            } else {
                                                // Remove the CartItem if quantity is zero
                                                return cartItemRepository.delete(cartItem)
                                                        .flatMap(aVoid -> {
                                                            // Update the total price of the cart
                                                            cart.setTotalPrice(cart.getTotalPrice() - (product.getPrice() * cartItem.getQuantity()));
                                                            return updateProductStockAndSaveCart(productId, -cartItem.getQuantity(), cart);
                                                        });
                                            }
                                        });
                            });
                })
                .flatMap(cart -> cartItemRepository.findByCartId(cartId)
                        .collectList()
                        .map(items -> {
                            CartResponse response = new CartResponse();
                            response.setCart(cart);
                            response.setItems(items);
                            return response;
                        }));
    }



    @Override
    public Mono<CartResponse> viewCartItems(Long cartId) {
        return cartRepository.findById(cartId)
                .switchIfEmpty(Mono.error(new RuntimeException("Cart not found")))
                .flatMap(cart -> cartItemRepository.findByCartId(cartId)
                        .collectList()
                        .map(items -> {
                            CartResponse response = new CartResponse();
                            response.setCart(cart);
                            response.setItems(items);
                            return response;
                        }));
    }


    // Helper method to update the product stock and save the updated cart
    // Helper method to update the product stock and save the updated cart
    private Mono<Cart> updateProductStockAndSaveCart(Long productId, int quantityChange, Cart cart) {
        return webClientBuilder.baseUrl(productServiceBaseUrl)
                .build()
                .get()
                .uri("/{productId}", productId)  // Fetch the product by its ID
                .retrieve()
                .bodyToMono(Product.class)
                .flatMap(product -> {
                    // Update the product stock
                    product.setQuantity(product.getQuantity() - quantityChange);
                    return webClientBuilder.baseUrl(productServiceBaseUrl)
                            .build()
                            .put()
                            .uri("/{productId}", product.getId())  // Update the product
                            .bodyValue(product)  // Send the updated product
                            .retrieve()
                            .bodyToMono(Product.class)
                            .then(cartRepository.save(cart));
                });
    }

}
