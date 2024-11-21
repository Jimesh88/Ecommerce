package com.cartservice.cart_service.dto;

import com.cartservice.cart_service.model.Cart;
import com.cartservice.cart_service.model.CartItem;
import java.util.List;

public class CartResponse {
    private Cart cart;
    private List<CartItem> items;

    // Getters and Setters
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}