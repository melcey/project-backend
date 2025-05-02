package com.cs308.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class WishlistResponse {
    private Long id;
    private Long userId;
    private List<WishlistItemResponse> wishlistItems;

    public WishlistResponse() {
        this.wishlistItems = new ArrayList<>();
    }

    public WishlistResponse(Long id, Long userId, List<WishlistItemResponse> wishlistItems) {
        this.id = id;
        this.userId = userId;
        this.wishlistItems = wishlistItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<WishlistItemResponse> getWishlistItems() {
        return wishlistItems;
    }

    public void setWishlistItems(List<WishlistItemResponse> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("WishlistResponse [id=").append(id)
            .append(", userId=").append(userId)
            .append(", wishlistItems=").append(wishlistItems)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((wishlistItems == null) ? 0 : wishlistItems.hashCode());
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
        WishlistResponse other = (WishlistResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (wishlistItems == null) {
            if (other.wishlistItems != null)
                return false;
        } else if (!wishlistItems.equals(other.wishlistItems))
            return false;
        return true;
    }
}
