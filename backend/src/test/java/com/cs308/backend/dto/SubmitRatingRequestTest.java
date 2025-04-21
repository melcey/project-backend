package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class SubmitRatingRequestTest {

    @Test
    void testConstructorAndGetters() {
        SubmitRatingRequest request = new SubmitRatingRequest(1L, 5);

        assertEquals(1L, request.getProductId());
        assertEquals(5, request.getRatingValue());
    }

    @Test
    void testSetters() {
        SubmitRatingRequest request = new SubmitRatingRequest();
        request.setProductId(2L);
        request.setRatingValue(4);

        assertEquals(2L, request.getProductId());
        assertEquals(4, request.getRatingValue());
    }

    @Test
    void testToString() {
        SubmitRatingRequest request = new SubmitRatingRequest(3L, 3);
        String expected = "SubmitRatingRequest [productId=3, ratingValue=3]";

        assertEquals(expected, request.toString());
    }

    @Test
    void testHashCodeAndEquals() {
        SubmitRatingRequest request1 = new SubmitRatingRequest(4L, 5);
        SubmitRatingRequest request2 = new SubmitRatingRequest(4L, 5);
        SubmitRatingRequest request3 = new SubmitRatingRequest(5L, 4);

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