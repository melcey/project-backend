package com.cs308.backend.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    pending("pending"),
    processing("processing"),
    shipped("shipped"),
    delivered("delivered"),
    cancelled("cancelled");

    private final String value;

    private OrderStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static OrderStatus fromString(String orderStatus) {
        for (OrderStatus status: OrderStatus.values()) {
            if (status.value.equalsIgnoreCase(orderStatus)) {
                return status;
            }
        }

        throw new IllegalArgumentException(String.format("Invalid order status: %s", orderStatus));
    }

    @Override
    public String toString() {
        return value;
    }
}
