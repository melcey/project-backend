package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ProductManagerRequestTest {
    @Test
    void testProductManagerRequestCreation() {
        ProductManagerRequest request = new ProductManagerRequest(1L, "John Doe");

        assertEquals(1L, request.getId());
        assertEquals("John Doe", request.getName());
    }

    @Test
    void testSettersAndGetters() {
        ProductManagerRequest request = new ProductManagerRequest();
        request.setId(1L);
        request.setName("John Doe");

        assertEquals(1L, request.getId());
        assertEquals("John Doe", request.getName());
    }

    @Test
    void testToString() {
        ProductManagerRequest request = new ProductManagerRequest(1L, "John Doe");
        String expected = "ProductManagerRequest [id=1, name=John Doe]";
        assertEquals(expected, request.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        ProductManagerRequest request1 = new ProductManagerRequest(1L, "John Doe");
        ProductManagerRequest request2 = new ProductManagerRequest(1L, "John Doe");
        ProductManagerRequest request3 = new ProductManagerRequest(2L, "Jane Smith");
        ProductManagerRequest requestNull = new ProductManagerRequest(null, null);

        // Test equality for objects with the same fields
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        // Test inequality for objects with different fields
        assertEquals(false, request1.equals(request3));
        assertEquals(false, request1.hashCode() == request3.hashCode());

        // Test equality for objects with all null fields
        ProductManagerRequest requestNull2 = new ProductManagerRequest(null, null);
        assertEquals(requestNull, requestNull2);
        assertEquals(requestNull.hashCode(), requestNull2.hashCode());

        // Test inequality with null
        assertEquals(false, request1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, request1.equals("someString"));

        // Test self-equality
        assertEquals(request1, request1);
    }
}