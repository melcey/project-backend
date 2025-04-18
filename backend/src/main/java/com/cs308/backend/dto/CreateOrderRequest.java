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
        this.status = null;
        this.totalPrice = null;
        this.deliveryAddress = null;
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
}
