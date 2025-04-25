package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class OrderStatusTest {
    @Test
    void testOrderStatusFromString() {
        assertEquals(OrderStatus.processing, OrderStatus.fromString("processing"));
        assertEquals(OrderStatus.processing, OrderStatus.fromString("processing"));
        assertEquals(OrderStatus.intransit, OrderStatus.fromString("in-transit"));
        assertEquals(OrderStatus.delivered, OrderStatus.fromString("delivered"));
    }

    @Test
    void testInvalidOrderStatusFromString() {
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.fromString("invalid_status"));
    }

    @Test
    void testOrderStatusToString() {
        assertEquals("processing", OrderStatus.processing.toString());
        assertEquals("processing", OrderStatus.processing.toString());
        assertEquals("in-transit", OrderStatus.intransit.toString());
        assertEquals("delivered", OrderStatus.delivered.toString());
    }

    @Test
    void testHashCodeAndEquals() {
        // Test equality for the same enum value
        assertEquals(OrderStatus.processing, OrderStatus.fromString("processing"));
        assertEquals(OrderStatus.processing.hashCode(), OrderStatus.fromString("processing").hashCode());

        // Test inequality for different enum values
        assertNotEquals(OrderStatus.processing, OrderStatus.delivered);
        assertNotEquals(OrderStatus.processing.hashCode(), OrderStatus.delivered.hashCode());

        // Test self-equality
        assertEquals(OrderStatus.processing, OrderStatus.processing);

        // Test inequality with null
        assertNotEquals(OrderStatus.intransit, null);

        // Test inequality with an object of a different class
        assertNotEquals(OrderStatus.delivered, "delivered");
    }
}