package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class RatingTest {
    @Test
    void testRatingCreation() {
        Product product = new Product();
        User user = new User();
        Rating rating = new Rating(product, user, 5);

        assertEquals(product, rating.getRatedProduct());
        assertEquals(user, rating.getRatingUser());
        assertEquals(5, rating.getRating());
        assertNotNull(rating.getRatingDate());
    }

    @Test
    void testSetRating() {
        Rating rating = new Rating();
        rating.setRating(4);

        assertEquals(4, rating.getRating());
    }

    @Test
    void testToString() {
        Product product = new Product();
        product.setName("Laptop");
        User user = new User();
        user.setName("John Doe");
        user.setRole(Role.customer);
        LocalDateTime ratingDate = LocalDateTime.of(2025, 4, 19, 12, 0);
        Rating rating = new Rating(product, user, 5);
        rating.setRatingDate(ratingDate);

        String expected = "Rating [id=null, ratedProduct=Product [id=null, name=Laptop, model=null, serialNumber=null, description=null, quantityInStock=0, price=null, warrantyStatus=null, distributorInfo=null, isActive=true, imageUrl=null, isPriced=false, originalPrice=null, discountRate=0, costPrice=null, category=null, productManager=null], ratedUser=User [id=null, name=John Doe, address=null, role=customer], rating=5, ratingDate=2025-04-19T12:00]";
        assertEquals(expected, rating.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {
        Product product1 = new Product();
        Product product2 = new Product();
        User user1 = new User();
        User user2 = new User();

        Rating rating1 = new Rating(product1, user1, 5);
        Rating rating2 = new Rating(product1, user1, 5);
        Rating rating3 = new Rating(product2, user2, 4);
        Rating ratingNull = new Rating();

        // Test equality for objects with the same fields
        rating1.setId(1L);
        rating2.setId(1L);
        assertEquals(rating1, rating2);
        assertEquals(rating1.hashCode(), rating2.hashCode());

        // Test inequality for objects with different fields
        rating3.setId(2L);
        assertEquals(false, rating1.equals(rating3));
        assertEquals(false, rating1.hashCode() == rating3.hashCode());

        // Test equality for objects with null fields
        Rating ratingNull2 = new Rating();
        assertEquals(ratingNull, ratingNull2);
        assertEquals(ratingNull.hashCode(), ratingNull2.hashCode());

        // Test inequality with null
        assertEquals(false, rating1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, rating1.equals("someString"));

        // Test self-equality
        assertEquals(rating1, rating1);
    }
}