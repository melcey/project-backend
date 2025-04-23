package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class CommentResponseTest {

    @Test
    void testConstructorAndGetters() {
        ProductResponse product = new ProductResponse(1L, "Laptop", null, null, null, 10, null, null, null, true, null, null);
        LocalDateTime commentDate = LocalDateTime.of(2025, 4, 21, 12, 0);
        CommentResponse response = new CommentResponse(1L, product, 2L, true, "Great product!", commentDate);

        assertEquals(1L, response.getId());
        assertEquals(product, response.getCommentedProduct());
        assertEquals(2L, response.getUserId());
        assertEquals(true, response.isApproved());
        assertEquals("Great product!", response.getComment());
        assertEquals(commentDate, response.getCommentDate());
    }

    @Test
    void testSetters() {
        ProductResponse product = new ProductResponse(2L, "Phone", null, null, null, 5, null, null, null, true, null, null);
        LocalDateTime commentDate = LocalDateTime.of(2025, 4, 21, 13, 0);
        CommentResponse response = new CommentResponse();

        response.setId(2L);
        response.setCommentedProduct(product);
        response.setUserId(3L);
        response.setApproved(false);
        response.setComment("Not satisfied.");
        response.setCommentDate(commentDate);

        assertEquals(2L, response.getId());
        assertEquals(product, response.getCommentedProduct());
        assertEquals(3L, response.getUserId());
        assertEquals(false, response.isApproved());
        assertEquals("Not satisfied.", response.getComment());
        assertEquals(commentDate, response.getCommentDate());
    }

    @Test
    void testToString() {
        ProductResponse product = new ProductResponse(3L, "Tablet", null, null, null, 8, null, null, null, true, null, null);
        LocalDateTime commentDate = LocalDateTime.of(2025, 4, 21, 14, 0);
        CommentResponse response = new CommentResponse(3L, product, 4L, true, "Amazing product!", commentDate);

        String expected = "CommentResponse [id=3, commentedProduct=ProductResponse [id=3, name=Tablet, model=null, serialNumber=null, description=null, quantityInStock=8, price=null, warrantyStatus=null, distributorInfo=null, isActive=true, imageUrl=null, category=null], userId=4, approved=true, comment=Amazing product!, commentDate=2025-04-21T14:00]";
        assertEquals(expected, response.toString());
    }

    @Test
    void testHashCodeAndEquals() {
        ProductResponse product1 = new ProductResponse(1L, "Laptop", null, null, null, 10, null, null, null, true, null, null);
        ProductResponse product2 = new ProductResponse(2L, "Phone", null, null, null, 5, null, null, null, true, null, null);

        LocalDateTime commentDate1 = LocalDateTime.of(2025, 4, 21, 12, 0);
        LocalDateTime commentDate2 = LocalDateTime.of(2025, 4, 21, 13, 0);

        CommentResponse response1 = new CommentResponse(1L, product1, 2L, true, "Great product!", commentDate1);
        CommentResponse response2 = new CommentResponse(1L, product1, 2L, true, "Great product!", commentDate1);
        CommentResponse response3 = new CommentResponse(2L, product2, 3L, false, "Not satisfied.", commentDate2);

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