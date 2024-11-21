package com.cartservice.cart_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("cart_item")
public class CartItem {

    @Id
    private Long id;  // The primary key field

    @Column("cart_id")  // Column to map the cart ID (foreign key)
    private Long cartId;  // This will be a foreign key pointing to the Cart

    @Column("product_id")  // Column to map the product ID (foreign key)
    private Long productId;  // This will be a foreign key pointing to the Product

    private int quantity;  // The quantity of the product in the cart

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
