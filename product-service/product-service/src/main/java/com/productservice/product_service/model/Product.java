package com.productservice.product_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("product")
public class Product {

    @Id
    private Long id;
    private String name;
    private double price;
    private int quantity; // This represents the stock quantity.

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Product( Long id,String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Product() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
