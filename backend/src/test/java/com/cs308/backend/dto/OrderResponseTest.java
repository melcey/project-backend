package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.cs308.backend.dao.OrderStatus;

class OrderResponseTest {
    @Test
    void testOrderResponseCreation() {
        CategoryResponse category = new CategoryResponse(1L, "Electronics", "Devices and gadgets");
        ProductResponse product = new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg", category);

        List<OrderItemResponse> items = new ArrayList<>();
        items.add(new OrderItemResponse(1L, 1L, product, 2, BigDecimal.valueOf(100.00)));

        LocalDateTime orderDate = LocalDateTime.of(2025, 4, 19, 12, 0);
        OrderResponse response = new OrderResponse(1L, 2L, orderDate, OrderStatus.pending,
                BigDecimal.valueOf(500.00), "123 Street", items);

        assertEquals(1L, response.getId());
        assertEquals(2L, response.getUserId());
        assertEquals(orderDate, response.getOrderDate());
        assertEquals(OrderStatus.pending, response.getStatus());
        assertEquals(BigDecimal.valueOf(500.00), response.getTotalPrice());
        assertEquals("123 Street", response.getDeliveryAddress());
        assertEquals(items, response.getOrderItems());
    }

    @Test
    void testSettersAndGetters() {
        List<OrderItemResponse> items = new ArrayList<>();
        OrderResponse response = new OrderResponse();
        response.setId(1L);
        response.setUserId(2L);
        response.setOrderDate(LocalDateTime.now());
        response.setStatus(OrderStatus.pending);
        response.setTotalPrice(BigDecimal.valueOf(500.00));
        response.setDeliveryAddress("123 Street");
        response.setOrderItems(items);

        assertEquals(1L, response.getId());
        assertEquals(2L, response.getUserId());
        assertNotNull(response.getOrderDate());
        assertEquals(OrderStatus.pending, response.getStatus());
        assertEquals(BigDecimal.valueOf(500.00), response.getTotalPrice());
        assertEquals("123 Street", response.getDeliveryAddress());
        assertEquals(items, response.getOrderItems());
    }

    @Test
    void testToString() {
        CategoryResponse category = new CategoryResponse(1L, "Electronics", "Devices and gadgets");
        ProductResponse product = new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg", category);

        List<OrderItemResponse> items = new ArrayList<>();
        items.add(new OrderItemResponse(1L, 1L, product, 2, BigDecimal.valueOf(100.00)));

        LocalDateTime orderDate = LocalDateTime.of(2025, 4, 19, 12, 0);
        OrderResponse response = new OrderResponse(1L, 2L, orderDate, OrderStatus.pending,
                BigDecimal.valueOf(500.00), "123 Street", items);

        String expected = "OrderResponse [id=1, userId=2, orderDate=2025-04-19T12:00, status=pending, totalPrice=500.0, deliveryAddress=123 Street, orderItems=[OrderItemResponse [id=1, orderId=1, product=ProductResponse [id=1, name=Laptop, model=Model X, serialNumber=12345, description=High-end laptop, quantityInStock=10, price=1200.0, warrantyStatus=1 year, distributorInfo=Distributor Inc., isActive=true, imageUrl=image.jpg, category=CategoryResponse [id=1, name=Electronics, description=Devices and gadgets]], quantity=2, price=100.0]]]";

        assertEquals(expected, response.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        ProductResponse product1 = new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg",
                new CategoryResponse(1L, "Electronics", "Devices and gadgets"));
        ProductResponse product2 = new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg",
                new CategoryResponse(1L, "Electronics", "Devices and gadgets"));
        ProductResponse product3 = new ProductResponse(2L, "Phone", "Model Y", "67890", "Smartphone", 5,
                BigDecimal.valueOf(800.00), "2 years", "Tech Distributors", false, "phone.jpg",
                new CategoryResponse(2L, "Mobiles", "Smart devices"));

        OrderItemResponse response1 = new OrderItemResponse(1L, 2L, product1, 3, BigDecimal.valueOf(150.00));
        OrderItemResponse response2 = new OrderItemResponse(1L, 2L, product2, 3, BigDecimal.valueOf(150.00));
        OrderItemResponse response3 = new OrderItemResponse(2L, 3L, product3, 5, BigDecimal.valueOf(200.00));
        OrderItemResponse responseNull = new OrderItemResponse(null, null, null, null, null);

        // Test equality for objects with the same fields
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        // Test inequality for objects with different fields
        assertEquals(false, response1.equals(response3));
        assertEquals(false, response1.hashCode() == response3.hashCode());

        // Test equality for objects with all null fields
        OrderItemResponse responseNull2 = new OrderItemResponse(null, null, null, null, null);
        assertEquals(responseNull, responseNull2);
        assertEquals(responseNull.hashCode(), responseNull2.hashCode());

        // Test inequality with null
        assertEquals(false, response1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, response1.equals("someString"));

        // Test self-equality
        assertEquals(response1, response1);
    }
}