package com.cs308.backend.dto;

import java.math.BigDecimal;

public class OrderItemResponse {
    private Long id;
    private Long orderId;
    private ProductResponse product;
    private Integer quantity;
    private BigDecimal price;

    public OrderItemResponse() {
        this.id = null;
        this.orderId = null;
        this.product = null;
        this.quantity = null;
        this.price = null;
    }

    public OrderItemResponse(Long id, Long orderId, ProductResponse product, Integer quantity, BigDecimal price) {
        this.id = id;
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    
}
