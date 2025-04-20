package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class OrderTest {
    @Test
    void testOrderCreation() {
        User user = new User();
        Order order = new Order(user, OrderStatus.pending, BigDecimal.valueOf(500.00), "456 Avenue", new ArrayList<>());

        assertEquals(user, order.getUser());
        assertEquals(OrderStatus.pending, order.getStatus());
        assertEquals(BigDecimal.valueOf(500.00), order.getTotalPrice());
        assertEquals("456 Avenue", order.getDeliveryAddress());
        assertNotNull(order.getOrderDate());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        List<OrderItem> items = new ArrayList<>();
        Order order = new Order();

        order.setId(1L);
        order.setUser(user);
        order.setStatus(OrderStatus.pending);
        order.setTotalPrice(BigDecimal.valueOf(500.00));
        order.setDeliveryAddress("456 Avenue");
        order.setOrderItems(items);

        assertEquals(1L, order.getId());
        assertEquals(user, order.getUser());
        assertEquals(OrderStatus.pending, order.getStatus());
        assertEquals(BigDecimal.valueOf(500.00), order.getTotalPrice());
        assertEquals("456 Avenue", order.getDeliveryAddress());
        assertEquals(items, order.getOrderItems());
    }

    @Test
    void testToString() {
        User user = new User();
        user.setName("John Doe");
        LocalDateTime orderDate = LocalDateTime.of(2025, 4, 19, 12, 0);
        Order order = new Order(user, OrderStatus.pending, BigDecimal.valueOf(500.00), "456 Avenue", new ArrayList<>());
        order.setOrderDate(orderDate);

        String expected = "Order [id=null, user=User [id=null, name=John Doe, address=null, role=null], orderDate=2025-04-19T12:00, status=pending, totalPrice=500.0, deliveryAddress=456 Avenue, orderItems=[]]";
        assertEquals(expected, order.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {
        User user1 = new User();
        User user2 = new User();

        Order order1 = new Order(user1, OrderStatus.pending, BigDecimal.valueOf(500.00), "456 Avenue", new ArrayList<>());
        Order order2 = new Order(user1, OrderStatus.pending, BigDecimal.valueOf(500.00), "456 Avenue", new ArrayList<>());
        Order order3 = new Order(user2, OrderStatus.delivered, BigDecimal.valueOf(1000.00), "789 Street", new ArrayList<>());
        Order orderNull = new Order();

        // Test equality for objects with the same fields
        order1.setId(1L);
        order2.setId(1L);
        assertEquals(order1, order2);
        assertEquals(order1.hashCode(), order2.hashCode());

        // Test inequality for objects with different fields
        order3.setId(2L);
        assertEquals(false, order1.equals(order3));
        assertEquals(false, order1.hashCode() == order3.hashCode());

        // Test equality for objects with null fields
        Order orderNull2 = new Order();
        assertEquals(orderNull, orderNull2);
        assertEquals(orderNull.hashCode(), orderNull2.hashCode());

        // Test inequality with null
        assertEquals(false, order1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, order1.equals("someString"));

        // Test self-equality
        assertEquals(order1, order1);
    }
}