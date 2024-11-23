package com.cartservice.cart_service.controller;



import com.cartservice.cart_service.dto.AddProductRequest;
import com.cartservice.cart_service.dto.CartResponse;
import com.cartservice.cart_service.model.Cart;
import com.cartservice.cart_service.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart API", description = "Operations related to the shopping cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{cartId}")
    @Operation(summary = "Add product to the cart", description = "Add a product to the cart by providing the product ID and quantity.")
    @ApiResponse(responseCode = "200", description = "Product added to cart successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Cart or product not found")
    public Mono<CartResponse> addProductToCart(            @Parameter(description = "The cart details for adding a product", required = true)
                                                               @RequestBody AddProductRequest addProductRequest){
        return cartService.addProductToCart(addProductRequest.getCartId(), addProductRequest.getProductId(), addProductRequest.getQuantity());

    }

    @DeleteMapping("/{cartId}")
    @ApiResponse(responseCode = "200", description = "Product removed from cart successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Cart or product not found")
    public Mono<CartResponse> removeProductFromCart(            @Parameter(description = "The cart details for removing a product", required = true)
                                                                    @RequestBody AddProductRequest addProductRequest) {
      return  cartService.removeProductFromCart(addProductRequest.getCartId(), addProductRequest.getProductId(), addProductRequest.getQuantity());
    }

    @GetMapping("/{cartId}")
    @Operation(summary = "View cart items", description = "Get the list of all products in the cart along with their quantities and prices.")
    @ApiResponse(responseCode = "200", description = "Cart items fetched successfully")
    @ApiResponse(responseCode = "404", description = "Cart not found")
    public Mono<CartResponse> viewCart(            @Parameter(description = "The ID of the cart to view", required = true)
                                                       @PathVariable Long cartId) {
        return cartService.viewCartItems(cartId);
    }
}
