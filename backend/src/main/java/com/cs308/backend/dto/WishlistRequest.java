package com.cs308.backend.dto;

public class WishlistRequest {
    private Long productId;

    public WishlistRequest() {}

    public WishlistRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
