package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class RatingResponseTest {

    @Test
    void testConstructorAndGetters() {
        ProductResponse product = new ProductResponse(1L, "Laptop", null, null, null, 10, null, null, null, true, null, null);
        LocalDateTime ratingDate = LocalDateTime.of(2025, 4, 21, 12, 0);
        RatingResponse response = new RatingResponse(1L, product, 2L, 5, ratingDate);

        assertEquals(1L, response.getId());
        assertEquals(product, response.getProduct());
        assertEquals(2L, response.getUserId());
        assertEquals(5, response.getRating());
        assertEquals(ratingDate, response.getRatingDate());
    }

    @Test
    void testSetters() {
        ProductResponse product = new ProductResponse(2L, "Phone", null, null, null, 5, null, null, null, true, null, null);
        LocalDateTime ratingDate = LocalDateTime.of(2025, 4, 21, 13, 0);
        RatingResponse response = new RatingResponse();

        response.setId(2L);
        response.setProduct(product);
        response.setUserId(3L);
        response.setRating(4);
        response.setRatingDate(ratingDate);

        assertEquals(2L, response.getId());
        assertEquals(product, response.getProduct());
        assertEquals(3L, response.getUserId());
        assertEquals(4, response.getRating());
        assertEquals(ratingDate, response.getRatingDate());
    }

    @Test
    void testToString() {
        ProductResponse product = new ProductResponse(3L, "Tablet", null, null, null, 8, null, null, null, true, null, null);
        LocalDateTime ratingDate = LocalDateTime.of(2025, 4, 21, 14, 0);
        RatingResponse response = new RatingResponse(3L, product, 4L, 3, ratingDate);

        String expected = "RatingResponse [id=3, product=ProductResponse [id=3, name=Tablet, model=null, serialNumber=null, description=null, quantityInStock=8, price=null, warrantyStatus=null, distributorInfo=null, isActive=true, imageUrl=null, category=null], userId=4, rating=3, ratingDate=2025-04-21T14:00]";
        assertEquals(expected, response.toString());
    }

    @Test
    void testHashCodeAndEquals() {
        ProductResponse product1 = new ProductResponse(1L, "Laptop", null, null, null, 10, null, null, null, true, null, null);
        ProductResponse product2 = new ProductResponse(2L, "Phone", null, null, null, 5, null, null, null, true, null, null);

        LocalDateTime ratingDate1 = LocalDateTime.of(2025, 4, 21, 12, 0);
        LocalDateTime ratingDate2 = LocalDateTime.of(2025, 4, 21, 13, 0);

        RatingResponse response1 = new RatingResponse(1L, product1, 2L, 5, ratingDate1);
        RatingResponse response2 = new RatingResponse(1L, product1, 2L, 5, ratingDate1);
        RatingResponse response3 = new RatingResponse(2L, product2, 3L, 4, ratingDate2);

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