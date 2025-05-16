package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ProductRequestTest {
    @Test
    void testCreateProductRequestCreation() {
        ProductRequest request = new ProductRequest("Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg", 1L);

        assertEquals("Laptop", request.getName());
        assertEquals("Model X", request.getModel());
        assertEquals("12345", request.getSerialNumber());
        assertEquals("High-end laptop", request.getDescription());
        assertEquals(10, request.getQuantityInStock());
        assertEquals(BigDecimal.valueOf(1200.00), request.getPrice());
        assertEquals("1 year", request.getWarrantyStatus());
        assertEquals("Distributor Inc.", request.getDistributorInfo());
        assertTrue(request.getIsActive());
        assertEquals("image.jpg", request.getImageUrl());
        assertEquals(1L, request.getCategoryId());
    }

    @Test
    void testSettersAndGetters() {
        ProductRequest request = new ProductRequest();
        request.setName("Laptop");
        request.setModel("Model X");
        request.setSerialNumber("12345");
        request.setDescription("High-end laptop");
        request.setQuantityInStock(10);
        request.setPrice(BigDecimal.valueOf(1200.00));
        request.setWarrantyStatus("1 year");
        request.setDistributorInfo("Distributor Inc.");
        request.setIsActive(true);
        request.setImageUrl("image.jpg");
        request.setCategory(1L);

        assertEquals("Laptop", request.getName());
        assertEquals("Model X", request.getModel());
        assertEquals("12345", request.getSerialNumber());
        assertEquals("High-end laptop", request.getDescription());
        assertEquals(10, request.getQuantityInStock());
        assertEquals(BigDecimal.valueOf(1200.00), request.getPrice());
        assertEquals("1 year", request.getWarrantyStatus());
        assertEquals("Distributor Inc.", request.getDistributorInfo());
        assertTrue(request.getIsActive());
        assertEquals("image.jpg", request.getImageUrl());
        assertEquals(1L, request.getCategoryId());
    }

    @Test
    void testToString() {
        ProductRequest request = new ProductRequest("Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg", 1L);
        String expected = "ProductRequest [name=Laptop, model=Model X, serialNumber=12345, description=High-end laptop, quantityInStock=10, price=1200.0, warrantyStatus=1 year, distributorInfo=Distributor Inc., isActive=true, imageUrl=image.jpg, categoryId=1]";
        assertEquals(expected, request.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        ProductRequest request1 = new ProductRequest("Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg", 1L);
        ProductRequest request2 = new ProductRequest("Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg", 1L);
        ProductRequest request3 = new ProductRequest("Phone", "Model Y", "67890", "Smartphone", 5,
                BigDecimal.valueOf(800.00), "2 years", "Tech Distributors", false, "phone.jpg", 2L);
        ProductRequest requestNull = new ProductRequest(null, null, null, null, 0, null, null, null, false, null, null);

        // Test equality for objects with the same fields
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        // Test inequality for objects with different fields
        assertEquals(false, request1.equals(request3));
        assertEquals(false, request1.hashCode() == request3.hashCode());

        // Test equality for objects with all null fields
        ProductRequest requestNull2 = new ProductRequest(null, null, null, null, 0, null, null, null, false, null, null);
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