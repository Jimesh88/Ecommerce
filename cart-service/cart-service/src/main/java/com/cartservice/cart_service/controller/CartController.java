package com.cartservice.cart_service.controller;



import com.cartservice.cart_service.dto.CartResponse;
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

    @PostMapping("/{cartId}/add/{productId}/{quantity}")
    public Mono<CartResponse> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable int quantity) {
        return cartService.addProductToCart(cartId, productId,quantity);

    }

    @DeleteMapping("/{cartId}/remove/{productId}/{quantity}")
    public Mono<CartResponse> removeProductFromCart(@PathVariable Long cartId, @PathVariable Long productId,@PathVariable int quantity) {
        return cartService.removeProductFromCart(cartId, productId,quantity);
    }

    @GetMapping("/{cartId}")
    public Mono<CartResponse> viewCart(@PathVariable Long cartId) {
        return cartService.viewCartItems(cartId);
    }
}
