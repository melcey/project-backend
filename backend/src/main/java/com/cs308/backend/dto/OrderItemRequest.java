package com.cs308.backend.dto;

import java.math.BigDecimal;

public class OrderItemRequest {
    private Integer quantity;
    private Long productId;
    private BigDecimal price;

    public OrderItemRequest() {}

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OrderItemRequest [quantity=").append(quantity)
            .append(", productId=").append(productId)
            .append(", price=").append(price)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((quantity == null) ? 0 : quantity.hashCode());
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
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
        OrderItemRequest other = (OrderItemRequest) obj;
        if (quantity == null) {
            if (other.quantity != null)
                return false;
        } else if (!quantity.equals(other.quantity))
            return false;
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
        return true;
    }
}
