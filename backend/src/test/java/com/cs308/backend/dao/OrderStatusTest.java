package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class OrderStatusTest {
    @Test
    void testOrderStatusFromString() {
        assertEquals(OrderStatus.pending, OrderStatus.fromString("pending"));
        assertEquals(OrderStatus.processing, OrderStatus.fromString("processing"));
        assertEquals(OrderStatus.shipped, OrderStatus.fromString("shipped"));
        assertEquals(OrderStatus.delivered, OrderStatus.fromString("delivered"));
        assertEquals(OrderStatus.cancelled, OrderStatus.fromString("cancelled"));
    }

    @Test
    void testInvalidOrderStatusFromString() {
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.fromString("invalid_status"));
    }

    @Test
    void testOrderStatusToString() {
        assertEquals("pending", OrderStatus.pending.toString());
        assertEquals("processing", OrderStatus.processing.toString());
        assertEquals("shipped", OrderStatus.shipped.toString());
        assertEquals("delivered", OrderStatus.delivered.toString());
        assertEquals("cancelled", OrderStatus.cancelled.toString());
    }

    @Test
    void testHashCodeAndEquals() {
        // Test equality for the same enum value
        assertEquals(OrderStatus.pending, OrderStatus.fromString("pending"));
        assertEquals(OrderStatus.pending.hashCode(), OrderStatus.fromString("pending").hashCode());

        // Test inequality for different enum values
        assertNotEquals(OrderStatus.pending, OrderStatus.delivered);
        assertNotEquals(OrderStatus.pending.hashCode(), OrderStatus.delivered.hashCode());

        // Test self-equality
        assertEquals(OrderStatus.processing, OrderStatus.processing);

        // Test inequality with null
        assertNotEquals(OrderStatus.shipped, null);

        // Test inequality with an object of a different class
        assertNotEquals(OrderStatus.cancelled, "cancelled");
    }
}