package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class RatingListResponseTest {

    @Test
    void testConstructorAndGetter() {
        ProductResponse product = new ProductResponse(1L, "Product Name", "Model X", "SN12345", "Description", 10,
                BigDecimal.valueOf(99.99), "1 year", "Distributor Info", true, "image.jpg", new CategoryResponse());
        RatingResponse rating1 = new RatingResponse(1L, product, 101L, 5, LocalDateTime.now());
        RatingResponse rating2 = new RatingResponse(2L, product, 102L, 3, LocalDateTime.now());
        List<RatingResponse> ratings = Arrays.asList(rating1, rating2);

        RatingListResponse response = new RatingListResponse(ratings);

        assertEquals(ratings, response.getRatings());
    }

    @Test
    void testSetter() {
        ProductResponse product = new ProductResponse(1L, "Product Name", "Model X", "SN12345", "Description", 10,
                BigDecimal.valueOf(99.99), "1 year", "Distributor Info", true, "image.jpg", new CategoryResponse());
        RatingResponse rating1 = new RatingResponse(1L, product, 101L, 5, LocalDateTime.now());
        RatingResponse rating2 = new RatingResponse(2L, product, 102L, 3, LocalDateTime.now());
        List<RatingResponse> ratings = Arrays.asList(rating1, rating2);

        RatingListResponse response = new RatingListResponse();
        response.setRatings(ratings);

        assertEquals(ratings, response.getRatings());
    }

    @Test
    void testToString() {
        ProductResponse product = new ProductResponse(1L, "Product Name", "Model X", "SN12345", "Description", 10,
                BigDecimal.valueOf(99.99), "1 year", "Distributor Info", true, "image.jpg", new CategoryResponse());
        RatingResponse rating1 = new RatingResponse(1L, product, 101L, 5, LocalDateTime.now());
        RatingResponse rating2 = new RatingResponse(2L, product, 102L, 3, LocalDateTime.now());
        List<RatingResponse> ratings = Arrays.asList(rating1, rating2);

        RatingListResponse response = new RatingListResponse(ratings);

        StringBuilder builder = new StringBuilder();
        builder.append("RatingListResponse [ratings=").append(ratings).append("]");
        String expected = builder.toString();

        assertEquals(expected, response.toString());
    }

    @Test
    void testHashCode() {
        ProductResponse product = new ProductResponse(1L, "Product Name", "Model X", "SN12345", "Description", 10,
                BigDecimal.valueOf(99.99), "1 year", "Distributor Info", true, "image.jpg", new CategoryResponse());
        RatingResponse rating1 = new RatingResponse(1L, product, 101L, 5, LocalDateTime.now());
        RatingResponse rating2 = new RatingResponse(2L, product, 102L, 3, LocalDateTime.now());
        List<RatingResponse> ratings = Arrays.asList(rating1, rating2);

        RatingListResponse response1 = new RatingListResponse(ratings);
        RatingListResponse response2 = new RatingListResponse(ratings);

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEquals() {
        ProductResponse product = new ProductResponse(1L, "Product Name", "Model X", "SN12345", "Description", 10,
                BigDecimal.valueOf(99.99), "1 year", "Distributor Info", true, "image.jpg", new CategoryResponse());
        RatingResponse rating1 = new RatingResponse(1L, product, 101L, 5, LocalDateTime.now());
        RatingResponse rating2 = new RatingResponse(2L, product, 102L, 3, LocalDateTime.now());
        List<RatingResponse> ratings = Arrays.asList(rating1, rating2);

        RatingListResponse response1 = new RatingListResponse(ratings);
        RatingListResponse response2 = new RatingListResponse(ratings);

        assertTrue(response1.equals(response2));
        assertTrue(response2.equals(response1));

        RatingListResponse response3 = new RatingListResponse();
        assertFalse(response1.equals(response3));
        assertFalse(response3.equals(response1));

        assertFalse(response1.equals(null));
        assertFalse(response1.equals("Not a RatingListResponse"));
    }
}