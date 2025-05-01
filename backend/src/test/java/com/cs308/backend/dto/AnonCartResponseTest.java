package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class AnonCartResponseTest {

    @Test
    void testConstructorAndGetters() {
        ProductResponse product = new ProductResponse(1L, "Laptop", null, null, null, 10, BigDecimal.valueOf(1000), null, null, true, null, null);
        AnonCartItemResponse item = new AnonCartItemResponse(1L, 1L, product, 2, BigDecimal.valueOf(2000));
        List<AnonCartItemResponse> items = new ArrayList<>();
        items.add(item);

        AnonCartResponse response = new AnonCartResponse(1L, BigDecimal.valueOf(2000), items);

        assertEquals(1L, response.getId());
        assertEquals(BigDecimal.valueOf(2000), response.getTotalPrice());
        assertEquals(items, response.getItems());
    }

    @Test
    void testSetters() {
        ProductResponse product = new ProductResponse(2L, "Phone", null, null, null, 5, BigDecimal.valueOf(500), null, null, true, null, null);
        AnonCartItemResponse item = new AnonCartItemResponse(2L, 2L, product, 3, BigDecimal.valueOf(1500));
        List<AnonCartItemResponse> items = new ArrayList<>();
        items.add(item);

        AnonCartResponse response = new AnonCartResponse();
        response.setId(2L);
        response.setTotalPrice(BigDecimal.valueOf(1500));
        response.setItems(items);
        LocalDateTime createdAt = LocalDateTime.of(2025, 4, 21, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 4, 21, 13, 0);
        response.setCreatedAt(createdAt);
        response.setUpdatedAt(updatedAt);

        assertEquals(2L, response.getId());
        assertEquals(BigDecimal.valueOf(1500), response.getTotalPrice());
        assertEquals(items, response.getItems());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    void testToString() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 4, 21, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 4, 21, 11, 0);

        ProductResponse product = new ProductResponse(3L, "Tablet", null, null, null, 8, BigDecimal.valueOf(800), null, null, true, null, null);
        AnonCartItemResponse item = new AnonCartItemResponse(3L, 3L, product, 1, BigDecimal.valueOf(800));
        item.setCreatedAt(createdAt);
        List<AnonCartItemResponse> items = new ArrayList<>();
        items.add(item);
        
        AnonCartResponse response = new AnonCartResponse(3L, BigDecimal.valueOf(800), items);
        response.setCreatedAt(createdAt);
        response.setUpdatedAt(updatedAt);

        StringBuilder expectedBuilder = new StringBuilder();
        expectedBuilder.append("AnonCartResponse [id=3, totalPrice=800, items=[AnonCartItemResponse [id=3, anonCartId=3, product=ProductResponse [id=3, name=Tablet, model=null, serialNumber=null, description=null, quantityInStock=8, price=800, warrantyStatus=null, distributorInfo=null, isActive=true, imageUrl=null, category=null], quantity=1, priceAtAddition=800, createdAt=").append(createdAt).append("]], createdAt=").append(createdAt).append(", updatedAt=").append(updatedAt).append("]");
        String expected = expectedBuilder.toString();
        
        assertEquals(expected, response.toString());
    }

    @Test
    void testHashCodeAndEquals() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 4, 21, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 4, 21, 11, 0);

        ProductResponse product1 = new ProductResponse(1L, "Laptop", null, null, null, 10, BigDecimal.valueOf(1000), null, null, true, null, null);
        ProductResponse product2 = new ProductResponse(2L, "Phone", null, null, null, 5, BigDecimal.valueOf(500), null, null, true, null, null);

        AnonCartItemResponse item1 = new AnonCartItemResponse(1L, 1L, product1, 2, BigDecimal.valueOf(2000));
        item1.setCreatedAt(createdAt);
        AnonCartItemResponse item2 = new AnonCartItemResponse(2L, 2L, product2, 3, BigDecimal.valueOf(1500));
        item2.setCreatedAt(createdAt);

        List<AnonCartItemResponse> items1 = new ArrayList<>();
        items1.add(item1);

        List<AnonCartItemResponse> items2 = new ArrayList<>();
        items2.add(item2);

        AnonCartResponse response1 = new AnonCartResponse(1L, BigDecimal.valueOf(2000), items1);
        response1.setCreatedAt(createdAt);
        response1.setUpdatedAt(updatedAt);
        AnonCartResponse response2 = new AnonCartResponse(1L, BigDecimal.valueOf(2000), items1);
        response2.setCreatedAt(createdAt);
        response2.setUpdatedAt(updatedAt);
        AnonCartResponse response3 = new AnonCartResponse(2L, BigDecimal.valueOf(1500), items2);
        response3.setCreatedAt(createdAt);
        response3.setUpdatedAt(updatedAt);

        // Test equality for objects with the same fields
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        // Test inequality for objects with different fields
        assertNotEquals(response1, response3);
        assertNotEquals(response1.hashCode(), response3.hashCode());

        // Test inequality with null
        assertNotEquals(response1, null);

        // Test inequality with an object of a different class
        assertNotEquals(response1, "someString");

        // Test self-equality
        assertEquals(response1, response1);
    }
}