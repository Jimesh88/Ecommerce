package com.cartservice.cart_service.dto;

import com.cartservice.cart_service.model.Cart;
import com.cartservice.cart_service.model.CartItem;
import java.util.List;

public class CartResponse {
    private Cart cart;
    private List<CartItemResponse> items;  // List of CartItemResponse

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }
}