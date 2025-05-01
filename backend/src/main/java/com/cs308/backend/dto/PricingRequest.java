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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PricingRequest [productId=").append(productId)
            .append(", price=").append(price)
            .append(", costPrice=").append(costPrice)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((costPrice == null) ? 0 : costPrice.hashCode());
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
        PricingRequest other = (PricingRequest) obj;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (costPrice == null) {
            if (other.costPrice != null)
                return false;
        } else if (!costPrice.equals(other.costPrice))
            return false;
        return true;
    }
}