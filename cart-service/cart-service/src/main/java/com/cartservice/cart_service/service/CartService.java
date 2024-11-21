package com.cartservice.cart_service.service;

import com.cartservice.cart_service.model.Cart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface CartService {
    public Mono<Cart> addProductToCart(Long cartid,Long productId, int quantity);
    public Mono<Cart> removeProductFromCart(Long cartid,Long productId, int quantity);
    public Mono<Cart> viewCartItems(Long cartid);

}
