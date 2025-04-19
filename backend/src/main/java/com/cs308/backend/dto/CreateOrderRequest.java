package com.cs308.backend.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderRequest {
    private String status;
    private BigDecimal totalPrice;
    private String deliveryAddress;

    private List<OrderItemRequest> orderItems;

    public CreateOrderRequest() {
        this.orderItems = new ArrayList<>();
    }

    public CreateOrderRequest(String status, BigDecimal totalPrice, String deliveryAddress,
            List<OrderItemRequest> orderItems) {
        this.status = status;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
        this.orderItems = orderItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemRequest> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CreateOrderRequest [status=").append(status)
            .append(", totalPrice=").append(totalPrice)
            .append(", deliveryAddress=").append(deliveryAddress)
            .append(", orderItems=").append(orderItems)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((totalPrice == null) ? 0 : totalPrice.hashCode());
        result = prime * result + ((deliveryAddress == null) ? 0 : deliveryAddress.hashCode());
        result = prime * result + ((orderItems == null) ? 0 : orderItems.hashCode());
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
        CreateOrderRequest other = (CreateOrderRequest) obj;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (totalPrice == null) {
            if (other.totalPrice != null)
                return false;
        } else if (!totalPrice.equals(other.totalPrice))
            return false;
        if (deliveryAddress == null) {
            if (other.deliveryAddress != null)
                return false;
        } else if (!deliveryAddress.equals(other.deliveryAddress))
            return false;
        if (orderItems == null) {
            if (other.orderItems != null)
                return false;
        } else if (!orderItems.equals(other.orderItems))
            return false;
        return true;
    }
}
