package com.cartservice.cart_service.service;

import com.cartservice.cart_service.dto.CartResponse;
import com.cartservice.cart_service.model.Cart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface CartService {
    public Mono<CartResponse> addProductToCart(Long cartid,Long productId, int quantity);
    public Mono<CartResponse> removeProductFromCart(Long cartid,Long productId, int quantity);
    public Mono<CartResponse> viewCartItems(Long cartid);

}
