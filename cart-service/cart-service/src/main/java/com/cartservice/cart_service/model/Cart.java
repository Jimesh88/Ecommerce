package com.cartservice.cart_service.model;

import org.springframework.data.annotation.Id;
import javax.persistence.Table;
import java.util.List;

@Table(name = "cart")
public class Cart {
    @Id
    private Long cartId;
    private List<Long> productIds;
    private double totalPrice;

    // Constructor
    public Cart(long cartId, List<Long> productIds, double totalPrice) {
        this.cartId = cartId;
        this.productIds = productIds;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public long getCartId() {
        return cartId;
    }

    public void setCartId(long cartId) {
        this.cartId = cartId;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Method to add a product ID to the cart
    public void addProduct(long productId) {
        if (!this.productIds.contains(productId)) {
            this.productIds.add(productId);
        }
    }

    // Method to remove a product ID from the cart
    public void removeProduct(long productId) {
        this.productIds.remove(productId);
    }

    // Method to calculate the total price (assuming you have a way to get product prices)
    public void calculateTotalPrice() {
        // Implementation depends on how you get the price of each product
    }
}
