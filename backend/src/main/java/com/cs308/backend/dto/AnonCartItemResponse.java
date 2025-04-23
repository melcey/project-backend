package com.cs308.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AnonCartItemResponse {
    private Long id;
    private Long anonCartId;
    private ProductResponse product;
    private int quantity;
    private BigDecimal priceAtAddition;
    private LocalDateTime createdAt;

    public AnonCartItemResponse() {}

    public AnonCartItemResponse(Long id, Long anonCartId, ProductResponse product, int quantity,
            BigDecimal priceAtAddition) {
        this.id = id;
        this.anonCartId = anonCartId;
        this.product = product;
        this.quantity = quantity;
        this.priceAtAddition = priceAtAddition;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnonCartId() {
        return anonCartId;
    }

    public void setAnonCartId(Long anonCartId) {
        this.anonCartId = anonCartId;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
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
        builder.append("AnonCartItemResponse [id=").append(id)
            .append(", anonCartId=").append(anonCartId)
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
        result = prime * result + ((anonCartId == null) ? 0 : anonCartId.hashCode());
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + quantity;
        result = prime * result + ((priceAtAddition == null) ? 0 : priceAtAddition.hashCode());
        result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
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
        AnonCartItemResponse other = (AnonCartItemResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (anonCartId == null) {
            if (other.anonCartId != null)
                return false;
        } else if (!anonCartId.equals(other.anonCartId))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        if (quantity != other.quantity)
            return false;
        if (priceAtAddition == null) {
            if (other.priceAtAddition != null)
                return false;
        } else if (!priceAtAddition.equals(other.priceAtAddition))
            return false;
        if (createdAt == null) {
            if (other.createdAt != null)
                return false;
        } else if (!createdAt.equals(other.createdAt))
            return false;
        return true;
    }
}
