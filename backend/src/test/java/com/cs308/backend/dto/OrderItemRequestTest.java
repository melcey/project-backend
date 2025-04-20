package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class OrderItemRequestTest {
    @Test
    void testOrderItemRequestCreation() {
        OrderItemRequest request = new OrderItemRequest(2, 1L, BigDecimal.valueOf(50.00));

        assertEquals(2, request.getQuantity());
        assertEquals(1L, request.getProductId());
        assertEquals(BigDecimal.valueOf(50.00), request.getPrice());
    }

    @Test
    void testSettersAndGetters() {
        OrderItemRequest request = new OrderItemRequest();
        request.setQuantity(2);
        request.setProductId(1L);
        request.setPrice(BigDecimal.valueOf(50.00));

        assertEquals(2, request.getQuantity());
        assertEquals(1L, request.getProductId());
        assertEquals(BigDecimal.valueOf(50.00), request.getPrice());
    }

    @Test
    void testToString() {
        OrderItemRequest request = new OrderItemRequest(2, 1L, BigDecimal.valueOf(50.00));
        String expected = "OrderItemRequest [quantity=2, productId=1, price=50.0]";
        assertEquals(expected, request.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        OrderItemRequest request1 = new OrderItemRequest(2, 1L, BigDecimal.valueOf(50.00));
        OrderItemRequest request2 = new OrderItemRequest(2, 1L, BigDecimal.valueOf(50.00));
        OrderItemRequest request3 = new OrderItemRequest(3, 2L, BigDecimal.valueOf(100.00));
        OrderItemRequest requestNull = new OrderItemRequest(null, null, null);

        // Test equality for objects with the same fields
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        // Test inequality for objects with different fields
        assertEquals(false, request1.equals(request3));
        assertEquals(false, request1.hashCode() == request3.hashCode());

        // Test equality for objects with all null fields
        OrderItemRequest requestNull2 = new OrderItemRequest(null, null, null);
        assertEquals(requestNull, requestNull2);
        assertEquals(requestNull.hashCode(), requestNull2.hashCode());

        // Test inequality with null
        assertEquals(false, request1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, request1.equals("someString"));

        // Test self-equality
        assertEquals(request1, request1);
    }
}