package com.cs308.user_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    // Added the string values
    customer("customer"),
    sales_manager("sales_manager"),
    product_manager("product_manager");

    private final String value;

    Role(String value) {
        this.value = value;
    }
    
    // Fixes for the serialization/deserialization process
    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Role fromString(String role) {
        for (Role r: Role.values()) {
            if (r.value.equalsIgnoreCase(role)) {
                return r;
            }
        }

        throw new IllegalArgumentException("Invalid role: " + role);
    }

    @Override
    public String toString() {
        return value;
    }
}
