package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class AnonCartItemResponseTest {

    @Test
    void testConstructorAndGetters() {
        ProductResponse product = new ProductResponse(1L, "Laptop", null, null, null, 0, BigDecimal.valueOf(1000), null, null, true, null, null);
        AnonCartItemResponse response = new AnonCartItemResponse(1L, 2L, product, 3, BigDecimal.valueOf(3000));

        assertEquals(1L, response.getId());
        assertEquals(2L, response.getAnonCartId());
        assertEquals(product, response.getProduct());
        assertEquals(3, response.getQuantity());
        assertEquals(BigDecimal.valueOf(3000), response.getPriceAtAddition());
    }

    @Test
    void testSetters() {
        ProductResponse product = new ProductResponse(2L, "Phone", null, null, null, 0, BigDecimal.valueOf(500), null, null, true, null, null);
        AnonCartItemResponse response = new AnonCartItemResponse();

        response.setId(2L);
        response.setAnonCartId(3L);
        response.setProduct(product);
        response.setQuantity(5);
        response.setPriceAtAddition(BigDecimal.valueOf(2500));
        LocalDateTime createdAt = LocalDateTime.of(2025, 4, 21, 12, 0);
        response.setCreatedAt(createdAt);

        assertEquals(2L, response.getId());
        assertEquals(3L, response.getAnonCartId());
        assertEquals(product, response.getProduct());
        assertEquals(5, response.getQuantity());
        assertEquals(BigDecimal.valueOf(2500), response.getPriceAtAddition());
        assertEquals(createdAt, response.getCreatedAt());
    }

    @Test
    void testToString() {
        ProductResponse product = new ProductResponse(3L, "Tablet", null, null, null, 0, BigDecimal.valueOf(800), null, null, true, null, null);
        LocalDateTime createdAt = LocalDateTime.of(2025, 4, 21, 10, 0);
        AnonCartItemResponse response = new AnonCartItemResponse(4L, 5L, product, 2, BigDecimal.valueOf(1600));
        response.setCreatedAt(createdAt);

        String expected = "AnonCartItemResponse [id=4, anonCartId=5, product=ProductResponse [id=3, name=Tablet, model=null, serialNumber=null, description=null, quantityInStock=0, price=800, warrantyStatus=null, distributorInfo=null, isActive=true, imageUrl=null, category=null], quantity=2, priceAtAddition=1600, createdAt=2025-04-21T10:00]";
        assertEquals(expected, response.toString());
    }

    @Test
    void testHashCodeAndEquals() {
        ProductResponse product1 = new ProductResponse(1L, "Laptop", null, null, null, 0, BigDecimal.valueOf(1000), null, null, true, null, null);
        ProductResponse product2 = new ProductResponse(2L, "Phone", null, null, null, 0, BigDecimal.valueOf(500), null, null, true, null, null);

        AnonCartItemResponse response1 = new AnonCartItemResponse(1L, 2L, product1, 3, BigDecimal.valueOf(3000));
        AnonCartItemResponse response2 = new AnonCartItemResponse(1L, 2L, product1, 3, BigDecimal.valueOf(3000));
        AnonCartItemResponse response3 = new AnonCartItemResponse(2L, 3L, product2, 5, BigDecimal.valueOf(2500));

        response1.setCreatedAt(LocalDateTime.of(2025, 4, 19, 12, 0));
        response2.setCreatedAt(LocalDateTime.of(2025, 4, 19, 12, 0));
        response3.setCreatedAt(LocalDateTime.of(2025, 4, 19, 12, 0));

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