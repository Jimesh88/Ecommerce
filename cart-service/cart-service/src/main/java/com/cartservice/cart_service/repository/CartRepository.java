package com.cartservice.cart_service.repository;

import com.cartservice.cart_service.model.Cart;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface CartRepository extends R2dbcRepository<Cart,Long> {
    public Mono<Cart> findById(Long id);
}
