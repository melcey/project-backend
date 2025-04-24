package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class CommentListResponseTest {

    @Test
    void testConstructorAndGetter() {
        ProductResponse product = new ProductResponse(1L, "Product Name", "Model X", "SN12345", "Description", 10,
                BigDecimal.valueOf(99.99), "1 year", "Distributor Info", true, "image.jpg", new CategoryResponse());
        CommentResponse comment1 = new CommentResponse(1L, product, 101L, true, "Great product!", LocalDateTime.now());
        CommentResponse comment2 = new CommentResponse(2L, product, 102L, false, "Not satisfied.", LocalDateTime.now());
        List<CommentResponse> comments = Arrays.asList(comment1, comment2);

        CommentListResponse response = new CommentListResponse(comments);

        assertEquals(comments, response.getComments());
    }

    @Test
    void testSetter() {
        ProductResponse product = new ProductResponse(1L, "Product Name", "Model X", "SN12345", "Description", 10,
                BigDecimal.valueOf(99.99), "1 year", "Distributor Info", true, "image.jpg", new CategoryResponse());
        CommentResponse comment1 = new CommentResponse(1L, product, 101L, true, "Great product!", LocalDateTime.now());
        CommentResponse comment2 = new CommentResponse(2L, product, 102L, false, "Not satisfied.", LocalDateTime.now());
        List<CommentResponse> comments = Arrays.asList(comment1, comment2);

        CommentListResponse response = new CommentListResponse();
        response.setComments(comments);

        assertEquals(comments, response.getComments());
    }

    @Test
    void testToString() {
        ProductResponse product = new ProductResponse(1L, "Product Name", "Model X", "SN12345", "Description", 10,
                BigDecimal.valueOf(99.99), "1 year", "Distributor Info", true, "image.jpg", new CategoryResponse());
        CommentResponse comment1 = new CommentResponse(1L, product, 101L, true, "Great product!", LocalDateTime.now());
        CommentResponse comment2 = new CommentResponse(2L, product, 102L, false, "Not satisfied.", LocalDateTime.now());
        List<CommentResponse> comments = Arrays.asList(comment1, comment2);

        CommentListResponse response = new CommentListResponse(comments);

        String expected = "CommentListResponse [comments=" + comments + "]";
        assertEquals(expected, response.toString());
    }

    @Test
    void testHashCode() {
        ProductResponse product = new ProductResponse(1L, "Product Name", "Model X", "SN12345", "Description", 10,
                BigDecimal.valueOf(99.99), "1 year", "Distributor Info", true, "image.jpg", new CategoryResponse());
        CommentResponse comment1 = new CommentResponse(1L, product, 101L, true, "Great product!", LocalDateTime.now());
        CommentResponse comment2 = new CommentResponse(2L, product, 102L, false, "Not satisfied.", LocalDateTime.now());
        List<CommentResponse> comments = Arrays.asList(comment1, comment2);

        CommentListResponse response1 = new CommentListResponse(comments);
        CommentListResponse response2 = new CommentListResponse(comments);

        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEquals() {
        ProductResponse product = new ProductResponse(1L, "Product Name", "Model X", "SN12345", "Description", 10,
                BigDecimal.valueOf(99.99), "1 year", "Distributor Info", true, "image.jpg", new CategoryResponse());
        CommentResponse comment1 = new CommentResponse(1L, product, 101L, true, "Great product!", LocalDateTime.now());
        CommentResponse comment2 = new CommentResponse(2L, product, 102L, false, "Not satisfied.", LocalDateTime.now());
        List<CommentResponse> comments = Arrays.asList(comment1, comment2);

        CommentListResponse response1 = new CommentListResponse(comments);
        CommentListResponse response2 = new CommentListResponse(comments);

        assertTrue(response1.equals(response2));
        assertTrue(response2.equals(response1));

        CommentListResponse response3 = new CommentListResponse();
        assertFalse(response1.equals(response3));
        assertFalse(response3.equals(response1));

        assertFalse(response1.equals(null));
        assertFalse(response1.equals("Not a CommentListResponse"));
    }
}