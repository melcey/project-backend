package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class SignUpRequestTest {
    @Test
    void testSignUpRequestCreation() {
        SignUpRequest request = new SignUpRequest("John Doe", "john@example.com", "password123", "123 Street", "customer");

        assertEquals("John Doe", request.getName());
        assertEquals("john@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        assertEquals("123 Street", request.getAddress());
        assertEquals("customer", request.getRole());
    }

    @Test
    void testSettersAndGetters() {
        SignUpRequest request = new SignUpRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");
        request.setAddress("123 Street");
        request.setRole("customer");

        assertEquals("John Doe", request.getName());
        assertEquals("john@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        assertEquals("123 Street", request.getAddress());
        assertEquals("customer", request.getRole());
    }

    @Test
    void testToString() {
        SignUpRequest request = new SignUpRequest("John Doe", "john@example.com", "password123", "123 Street", "customer");
        String expected = "SignUpRequest [name=John Doe, email=john@example.com, password=password123, address=123 Street, role=customer]";
        assertEquals(expected, request.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        CategoryResponse category1 = new CategoryResponse(1L, "Electronics", "Devices and gadgets");
        CategoryResponse category2 = new CategoryResponse(1L, "Electronics", "Devices and gadgets");
        CategoryResponse category3 = new CategoryResponse(2L, "Mobiles", "Smart devices");

        ProductResponse response1 = new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg", category1);
        ProductResponse response2 = new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg", category2);
        ProductResponse response3 = new ProductResponse(2L, "Phone", "Model Y", "67890", "Smartphone", 5,
                BigDecimal.valueOf(800.00), "2 years", "Tech Distributors", false, "phone.jpg", category3);
        ProductResponse responseNull = new ProductResponse(null, null, null, null, null, 0, null, null, null, false, null, null);

        // Test equality for objects with the same fields
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        // Test inequality for objects with different fields
        assertEquals(false, response1.equals(response3));
        assertEquals(false, response1.hashCode() == response3.hashCode());

        // Test equality for objects with all null fields
        ProductResponse responseNull2 = new ProductResponse(null, null, null, null, null, 0, null, null, null, false, null, null);
        assertEquals(responseNull, responseNull2);
        assertEquals(responseNull.hashCode(), responseNull2.hashCode());

        // Test inequality with null
        assertEquals(false, response1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, response1.equals("someString"));

        // Test self-equality
        assertEquals(response1, response1);
    }
}