package com.cs308.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cs308.backend.dao.OrderStatus;

public class OrderResponse {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private String deliveryAddress;
    private List<OrderItemResponse> orderItems;

    public OrderResponse() {
        this.orderItems = new ArrayList<>();
    }

    public OrderResponse(Long id, Long userId, LocalDateTime orderDate, OrderStatus status, BigDecimal totalPrice,
            String deliveryAddress, List<OrderItemResponse> orderItems) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.status = status;
        this.totalPrice = totalPrice;
        this.deliveryAddress = deliveryAddress;
        this.orderItems = orderItems;
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

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
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

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OrderResponse [id=").append(id)
            .append(", userId=").append(userId)
            .append(", orderDate=").append(orderDate)
            .append(", status=").append(status)
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
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((orderDate == null) ? 0 : orderDate.hashCode());
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
        OrderResponse other = (OrderResponse) obj;
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
        if (orderDate == null) {
            if (other.orderDate != null)
                return false;
        } else if (!orderDate.equals(other.orderDate))
            return false;
        if (status != other.status)
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
