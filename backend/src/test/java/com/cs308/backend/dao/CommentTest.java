package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class CommentTest {
    @Test
    void testCommentCreation() {
        Product product = new Product();
        User user = new User();
        Comment comment = new Comment(product, user, "Great product!");

        assertEquals(product, comment.getCommentedProduct());
        assertEquals(user, comment.getCommentingUser());
        assertEquals("Great product!", comment.getComment());
        assertFalse(comment.getApproved());
        assertNotNull(comment.getCommentDate());
    }

    @Test
    void testSettersAndGetters() {
        Product product = new Product();
        User user = new User();
        Comment comment = new Comment();

        comment.setId(1L);
        comment.setCommentedProduct(product);
        comment.setCommentingUser(user);
        comment.setComment("Great product!");
        comment.setApproved(true);

        assertEquals(1L, comment.getId());
        assertEquals(product, comment.getCommentedProduct());
        assertEquals(user, comment.getCommentingUser());
        assertEquals("Great product!", comment.getComment());
        assertTrue(comment.getApproved());
    }

    @Test
    void testToString() {
        Product product = new Product();
        product.setName("Laptop");
        User user = new User();
        user.setName("John Doe");
        user.setRole(Role.customer);
        LocalDateTime commentDate = LocalDateTime.of(2025, 4, 19, 12, 0);
        Comment comment = new Comment(product, user, "Great product!");
        comment.setCommentDate(commentDate);

        String expected = "Comment [id=null, commentedProduct=Product [id=null, name=Laptop, model=null, serialNumber=null, description=null, quantityInStock=0, price=null, warrantyStatus=null, distributorInfo=null, isActive=true, imageUrl=null, isPriced=false, originalPrice=null, discountRate=0, costPrice=null, category=null, productManager=null], commentingUser=User [id=null, name=John Doe, address=null, role=customer], approved=false, comment=Great product!, commentDate=2025-04-19T12:00]";
        assertEquals(expected, comment.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {
        Product product1 = new Product();
        Product product2 = new Product();
        User user1 = new User();
        User user2 = new User();

        Comment comment1 = new Comment(product1, user1, "Great product!");
        Comment comment2 = new Comment(product1, user1, "Great product!");
        Comment comment3 = new Comment(product2, user2, "Not satisfied.");
        Comment commentNull = new Comment();

        // Test equality for objects with the same fields
        comment1.setId(1L);
        comment2.setId(1L);
        assertEquals(comment1, comment2);
        assertEquals(comment1.hashCode(), comment2.hashCode());

        // Test inequality for objects with different fields
        comment3.setId(2L);
        assertEquals(false, comment1.equals(comment3));
        assertEquals(false, comment1.hashCode() == comment3.hashCode());

        // Test equality for objects with null fields
        Comment commentNull2 = new Comment();
        assertEquals(commentNull, commentNull2);
        assertEquals(commentNull.hashCode(), commentNull2.hashCode());

        // Test inequality with null
        assertEquals(false, comment1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, comment1.equals("someString"));

        // Test self-equality
        assertEquals(comment1, comment1);
    }
}