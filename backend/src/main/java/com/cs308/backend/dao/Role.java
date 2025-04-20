package com.cs308.backend.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    // Added the string values
    customer("customer"),
    sales_manager("sales_manager"),
    product_manager("product_manager");

    private final String value;

    // An enum constructor in Java should always be private (it is implicitly private if you do not specify)
    private Role(String value) {
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

        throw new IllegalArgumentException(String.format("Invalid role: %s", role));
    }

    @Override
    public String toString() {
        return value;
    }
}
