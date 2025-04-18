package com.cs308.backend.dto;

import java.math.BigDecimal;

public class OrderItemRequest {
    private Integer quantity;
    private Long productId;
    private BigDecimal price;

    public OrderItemRequest() {
        this.quantity = null;
        this.productId = null;
        this.price = null;
    }

    public OrderItemRequest(Integer quantity, Long productId, BigDecimal price) {
        this.quantity = quantity;
        this.productId = productId;
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
