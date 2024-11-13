package com.cartservice.cart_service.controller;



import com.cartservice.cart_service.model.Cart;
import com.cartservice.cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{cartId}/add/{productId}")
    public Mono<Cart> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId) {
        return cartService.addProductToCart(cartId, productId);
    }

    @DeleteMapping("/{cartId}/remove/{productId}")
    public Mono<Cart> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        return cartService.removeProductFromCart(cartId, productId);
    }

    @GetMapping("/{cartId}")
    public Mono<Cart> viewCart(@PathVariable Long cartId) {
        return cartService.viewCartItems(cartId);
    }
}
