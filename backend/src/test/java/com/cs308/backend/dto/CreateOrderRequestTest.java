package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class CreateOrderRequestTest {
    @Test
    void testCreateOrderRequestCreation() {
        List<OrderItemRequest> orderItems = new ArrayList<>();
        CreateOrderRequest request = new CreateOrderRequest("Pending", BigDecimal.valueOf(500.00), "123 Street", orderItems);

        assertEquals("Pending", request.getStatus());
        assertEquals(BigDecimal.valueOf(500.00), request.getTotalPrice());
        assertEquals("123 Street", request.getDeliveryAddress());
        assertEquals(orderItems, request.getOrderItems());
    }

    @Test
    void testSettersAndGetters() {
        CreateOrderRequest request = new CreateOrderRequest();
        List<OrderItemRequest> items = new ArrayList<>();
        request.setStatus("Pending");
        request.setTotalPrice(BigDecimal.valueOf(100.50));
        request.setDeliveryAddress("123 Street");
        request.setOrderItems(items);

        assertEquals("Pending", request.getStatus());
        assertEquals(BigDecimal.valueOf(100.50), request.getTotalPrice());
        assertEquals("123 Street", request.getDeliveryAddress());
        assertEquals(items, request.getOrderItems());
    }

    @Test
    void testToString() {
        List<OrderItemRequest> items = new ArrayList<>();
        CreateOrderRequest request = new CreateOrderRequest("Pending", BigDecimal.valueOf(100.50), "123 Street", items);
        String expected = "CreateOrderRequest [status=Pending, totalPrice=100.5, deliveryAddress=123 Street, orderItems=[]]";
        assertEquals(expected, request.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        List<OrderItemRequest> items1 = new ArrayList<>();
        List<OrderItemRequest> items2 = new ArrayList<>();
        List<OrderItemRequest> items3 = new ArrayList<>();
        items3.add(new OrderItemRequest()); // Adding a dummy item for inequality testing

        CreateOrderRequest request1 = new CreateOrderRequest("Pending", BigDecimal.valueOf(100.50), "123 Street", items1);
        CreateOrderRequest request2 = new CreateOrderRequest("Pending", BigDecimal.valueOf(100.50), "123 Street", items2);
        CreateOrderRequest request3 = new CreateOrderRequest("Completed", BigDecimal.valueOf(200.00), "456 Avenue", items3);
        CreateOrderRequest requestNull = new CreateOrderRequest(null, null, null, null);

        // Test equality for objects with the same fields
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        // Test inequality for objects with different fields
        assertEquals(false, request1.equals(request3));
        assertEquals(false, request1.hashCode() == request3.hashCode());

        // Test equality for objects with all null fields
        CreateOrderRequest requestNull2 = new CreateOrderRequest(null, null, null, null);
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