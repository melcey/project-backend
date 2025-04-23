package com.cs308.backend.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "anon_cart_items", schema = "public")
public class AnonCartItem implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private AnonCart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price_at_addition", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtAddition;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AnonCartItem() {}

    public AnonCartItem(AnonCart cart, Product product, int quantity, BigDecimal priceAtAddition) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.priceAtAddition = priceAtAddition;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AnonCart getCart() {
        return cart;
    }

    public void setCart(AnonCart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtAddition() {
        return priceAtAddition;
    }

    public void setPriceAtAddition(BigDecimal priceAtAddition) {
        this.priceAtAddition = priceAtAddition;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AnonCartItem [id=").append(id)
            .append(", cart=").append(cart)
            .append(", product=").append(product)
            .append(", quantity=").append(quantity)
            .append(", priceAtAddition=").append(priceAtAddition)
            .append(", createdAt=").append(createdAt)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AnonCartItem other = (AnonCartItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public AnonCartItem clone() {
        try {
            AnonCartItem clonedItem = (AnonCartItem) super.clone();
            // Deep copy mutable fields
            clonedItem.setPriceAtAddition(new BigDecimal(this.priceAtAddition.toString())); 
            clonedItem.setCart(null);
            return clonedItem;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}