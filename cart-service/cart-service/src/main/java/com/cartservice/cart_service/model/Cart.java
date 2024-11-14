package com.cartservice.cart_service.model;

import org.springframework.data.annotation.Id;
import javax.persistence.Table;
import java.util.List;

@Table(name = "cart")
public class Cart {
    @Id
    private Long cartId;
    private Long productId;
    private double totalPrice;

    // Constructor
    public Cart(long cartId,Long productId, double totalPrice) {
        this.cartId = cartId;
        this.productId = productId;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public Long getproductId() {
        return productId;
    }

    public void setproductId(Long productId) {
        this.productId = productId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }



    // Method to calculate the total price (assuming you have a way to get product prices)
    public void calculateTotalPrice() {
        // Implementation depends on how you get the price of each product
    }
}
