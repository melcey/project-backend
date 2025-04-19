package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UpdateOrderStateRequestTest {
    @Test
    void testUpdateOrderStateRequestCreation() {
        UpdateOrderStateRequest request = new UpdateOrderStateRequest("shipped");

        assertEquals("shipped", request.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        UpdateOrderStateRequest request = new UpdateOrderStateRequest();
        request.setStatus("shipped");

        assertEquals("shipped", request.getStatus());
    }

    @Test
    void testToString() {
        UpdateOrderStateRequest request = new UpdateOrderStateRequest("shipped");
        String expected = "UpdateOrderStateRequest [status=shipped]";
        assertEquals(expected, request.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        UpdateOrderStateRequest request1 = new UpdateOrderStateRequest("shipped");
        UpdateOrderStateRequest request2 = new UpdateOrderStateRequest("shipped");
        UpdateOrderStateRequest request3 = new UpdateOrderStateRequest("delivered");
        UpdateOrderStateRequest requestNull = new UpdateOrderStateRequest(null);

        // Test equality for objects with the same status
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        // Test inequality for objects with different statuses
        assertEquals(false, request1.equals(request3));
        assertEquals(false, request1.hashCode() == request3.hashCode());

        // Test equality for objects with null status
        UpdateOrderStateRequest requestNull2 = new UpdateOrderStateRequest(null);
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