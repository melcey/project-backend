package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class UpdateProductRequestTest {
    @Test
    void testUpdateProductRequestCreation() {
        UpdateProductRequest request = new UpdateProductRequest("Laptop", "Model X", "12345", "Updated description", 20,
                BigDecimal.valueOf(1500.00), "2 years", "Updated Distributor", false, "image.jpg", 1L);

        assertEquals("Laptop", request.getName());
        assertEquals("Model X", request.getModel());
        assertEquals("12345", request.getSerialNumber());
        assertEquals("Updated description", request.getDescription());
        assertEquals(20, request.getQuantityInStock());
        assertEquals(BigDecimal.valueOf(1500.00), request.getPrice());
        assertEquals("2 years", request.getWarrantyStatus());
        assertEquals("Updated Distributor", request.getDistributorInfo());
        assertFalse(request.getIsActive());
        assertEquals("image.jpg", request.getImageUrl());
        assertEquals(1L, request.getCategoryId());
    }

    @Test
    void testSettersAndGetters() {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Laptop");
        request.setModel("Model X");
        request.setSerialNumber("12345");
        request.setDescription("Updated description");
        request.setQuantityInStock(20);
        request.setPrice(BigDecimal.valueOf(1500.00));
        request.setWarrantyStatus("2 years");
        request.setDistributorInfo("Updated Distributor");
        request.setIsActive(false);
        request.setImageUrl("image.jpg");
        request.setCategoryId(1L);

        assertEquals("Laptop", request.getName());
        assertEquals("Model X", request.getModel());
        assertEquals("12345", request.getSerialNumber());
        assertEquals("Updated description", request.getDescription());
        assertEquals(20, request.getQuantityInStock());
        assertEquals(BigDecimal.valueOf(1500.00), request.getPrice());
        assertEquals("2 years", request.getWarrantyStatus());
        assertEquals("Updated Distributor", request.getDistributorInfo());
        assertFalse(request.getIsActive());
        assertEquals("image.jpg", request.getImageUrl());
        assertEquals(1L, request.getCategoryId());
    }

    @Test
    void testToString() {
        UpdateProductRequest request = new UpdateProductRequest("Laptop", "Model X", "12345", "Updated description", 20,
                BigDecimal.valueOf(1500.00), "2 years", "Updated Distributor", false, "image.jpg", 1L);
        String expected = "UpdateProductRequest [name=Laptop, model=Model X, serialNumber=12345, description=Updated description, quantityInStock=20, price=1500.0, warrantyStatus=2 years, distributorInfo=Updated Distributor, isActive=false, imageUrl=image.jpg, categoryId=1]";
        assertEquals(expected, request.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        UpdateProductRequest request1 = new UpdateProductRequest("Laptop", "Model X", "12345", "Updated description", 20,
                BigDecimal.valueOf(1500.00), "2 years", "Updated Distributor", false, "image.jpg", 1L);
        UpdateProductRequest request2 = new UpdateProductRequest("Laptop", "Model X", "12345", "Updated description", 20,
                BigDecimal.valueOf(1500.00), "2 years", "Updated Distributor", false, "image.jpg", 1L);
        UpdateProductRequest request3 = new UpdateProductRequest("Phone", "Model Y", "67890", "New description", 10,
                BigDecimal.valueOf(800.00), "1 year", "New Distributor", true, "phone.jpg", 2L);
        UpdateProductRequest requestNull = new UpdateProductRequest(null, null, null, null, null, null, null, null, null, null, null);

        // Test equality for objects with the same fields
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());

        // Test inequality for objects with different fields
        assertEquals(false, request1.equals(request3));
        assertEquals(false, request1.hashCode() == request3.hashCode());

        // Test equality for objects with all null fields
        UpdateProductRequest requestNull2 = new UpdateProductRequest(null, null, null, null, null, null, null, null, null, null, null);
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