package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class CommentRequestTest {

    @Test
    void testConstructorAndGetters() {
        CommentRequest request = new CommentRequest(1L, "Great product!");

        assertEquals(1L, request.getProductId());
        assertEquals("Great product!", request.getComment());
    }

    @Test
    void testSetters() {
        CommentRequest request = new CommentRequest();
        request.setProductId(2L);
        request.setComment("Not satisfied with the quality.");

        assertEquals(2L, request.getProductId());
        assertEquals("Not satisfied with the quality.", request.getComment());
    }

    @Test
    void testToString() {
        CommentRequest request = new CommentRequest(3L, "Amazing value for the price.");
        String expected = "CommentRequest [productId=3, comment=Amazing value for the price.]";

        assertEquals(expected, request.toString());
    }

    @Test
    void testHashCodeAndEquals() {
        CommentRequest request1 = new CommentRequest(4L, "Highly recommend!");
        CommentRequest request2 = new CommentRequest(4L, "Highly recommend!");
        CommentRequest request3 = new CommentRequest(5L, "Not worth the price.");

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