package com.cs308.backend.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlist", schema = "public")
public class Wishlist implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "notification_sent")
    private boolean isNotificationSent = false;

    @Column(name = "last_notified_at")
    private LocalDateTime lastNotifiedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishlistItem> wishlistItems;

    public Wishlist() {
        this.wishlistItems = new ArrayList<>();
    }

    public Wishlist(User user) {
        this.user = user;
        this.wishlistItems = new ArrayList<>();
    }

    public Wishlist(User user, List<WishlistItem> wishlistItems) {
        this.user = user;
        this.wishlistItems = wishlistItems;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<WishlistItem> getWishlistItems() {
        return wishlistItems;
    }

    public void setWishlistItems(List<WishlistItem> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean getIsNotificationSent() {
        return isNotificationSent;
    }

    public void setIsNotificationSent(boolean isNotificationSent) {
        this.isNotificationSent = isNotificationSent;
    }

    public LocalDateTime getLastNotifiedAt() {
        return lastNotifiedAt;
    }

    public void setLastNotifiedAt(LocalDateTime lastNotifiedAt) {
        this.lastNotifiedAt = lastNotifiedAt;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Wishlist [id=").append(id)
            .append(", user=").append(user)
            .append(", createdAt=").append(createdAt)
            .append(", isNotificationSent=").append(isNotificationSent)
            .append(", lastNotifiedAt=").append(lastNotifiedAt)
            .append(", wishlistItems=").append(wishlistItems)
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
        Wishlist other = (Wishlist) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public Wishlist clone() {
        try {
            Wishlist clonedWishlist = (Wishlist) super.clone();
            // Deep copy the items list
            List<WishlistItem> clonedItems = new ArrayList<>();
            for (WishlistItem item : this.wishlistItems) {
                clonedItems.add(item.clone());
            }
            clonedWishlist.setWishlistItems(clonedItems);
            for (WishlistItem clonedItem : clonedWishlist.wishlistItems) {
                clonedItem.setWishlist(clonedWishlist);
            }
            return clonedWishlist;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}
