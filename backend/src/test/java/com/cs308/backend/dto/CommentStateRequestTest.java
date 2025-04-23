package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class CommentStateRequestTest {

    @Test
    void testConstructorAndGetters() {
        CommentStateRequest request = new CommentStateRequest(1L);

        assertEquals(1L, request.getCommentId());
    }

    @Test
    void testSetters() {
        CommentStateRequest request = new CommentStateRequest();
        request.setCommentId(2L);

        assertEquals(2L, request.getCommentId());
    }

    @Test
    void testToString() {
        CommentStateRequest request = new CommentStateRequest(3L);
        String expected = "CommentStateRequest [commentId=3]";

        assertEquals(expected, request.toString());
    }

    @Test
    void testHashCodeAndEquals() {
        CommentStateRequest request1 = new CommentStateRequest(4L);
        CommentStateRequest request2 = new CommentStateRequest(4L);
        CommentStateRequest request3 = new CommentStateRequest(5L);

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