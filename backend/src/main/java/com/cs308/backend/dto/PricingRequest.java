package com.cs308.backend.dto;

import java.math.BigDecimal;

public class PricingRequest {
    private Long productId;
    private BigDecimal price;
    private BigDecimal costPrice;
    
    public PricingRequest() {}
    
    public PricingRequest(Long productId, BigDecimal price, BigDecimal costPrice) {
        this.productId = productId;
        this.price = price;
        this.costPrice = costPrice;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getCostPrice() {
        return costPrice;
    }
    
    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }
}