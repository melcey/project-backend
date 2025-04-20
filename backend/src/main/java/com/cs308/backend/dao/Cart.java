package com.cs308.backend.dao;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts", schema = "public")
public class Cart implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Cart() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Cart(User user, BigDecimal totalPrice, List<CartItem> items) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.items = items;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Cart [id=").append(id)
            .append(", user=").append(user)
            .append(", totalPrice=").append(totalPrice)
            .append(", items=").append(items)
            .append(", createdAt=").append(createdAt)
            .append(", updatedAt=").append(updatedAt)
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
        Cart other = (Cart) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public Cart clone() {
        try {
            Cart clonedCart = (Cart) super.clone();
            // Deep copy the items list
            List<CartItem> clonedItems = new ArrayList<>();
            for (CartItem item : this.items) {
                clonedItems.add(item.clone());
            }
            clonedCart.setItems(clonedItems);
            for (CartItem clonedItem : clonedCart.items) {
                clonedItem.setCart(clonedCart);
            }
            return clonedCart;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}