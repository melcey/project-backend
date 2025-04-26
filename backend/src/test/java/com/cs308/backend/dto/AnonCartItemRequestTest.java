package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class AnonCartItemRequestTest {

    @Test
    void testConstructorAndGetters() {
        AnonCartItemRequest request = new AnonCartItemRequest(1L, 5);

        assertEquals(1L, request.getProductId());
        assertEquals(5, request.getQuantity());
    }

    @Test
    void testSetters() {
        AnonCartItemRequest request = new AnonCartItemRequest();
        request.setProductId(2L);
        request.setQuantity(10);

        assertEquals(2L, request.getProductId());
        assertEquals(10, request.getQuantity());
    }

    @Test
    void testToString() {
        AnonCartItemRequest request = new AnonCartItemRequest(3L, 7);
        String expected = "AnonCartItemRequest [productId=3, quantity=7]";

        assertEquals(expected, request.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        AnonCartItemRequest request1 = new AnonCartItemRequest(4L, 8);
        AnonCartItemRequest request2 = new AnonCartItemRequest(4L, 8);
        AnonCartItemRequest request3 = new AnonCartItemRequest(5L, 9);

        // Test equality for objects with the same fields
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        // Test inequality for objects with different fields
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());

        // Test inequality with null
        assertNotEquals(request1, null);

        // Test inequality with an object of a different class
        assertNotEquals(request1, "someString");

        // Test self-equality
        assertEquals(request1, request1);
    }
}