package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ProductResponseTest {
    @Test
    void testProductResponseCreation() {
        CategoryResponse category = new CategoryResponse(1L, "Electronics", "Devices and gadgets");
        ProductResponse product = new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg", category);

        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals("Model X", product.getModel());
        assertEquals("12345", product.getSerialNumber());
        assertEquals("High-end laptop", product.getDescription());
        assertEquals(10, product.getQuantityInStock());
        assertEquals(BigDecimal.valueOf(1200.00), product.getPrice());
        assertEquals("1 year", product.getWarrantyStatus());
        assertEquals("Distributor Inc.", product.getDistributorInfo());
        assertTrue(product.getIsActive());
        assertEquals("image.jpg", product.getImageUrl());
        assertEquals(category, product.getCategory());
    }

    @Test
    void testSettersAndGetters() {
        ProductResponse response = new ProductResponse();
        CategoryResponse category = new CategoryResponse();

        response.setId(1L);
        response.setName("Laptop");
        response.setModel("Model X");
        response.setSerialNumber("12345");
        response.setDescription("High-end laptop");
        response.setQuantityInStock(10);
        response.setPrice(BigDecimal.valueOf(1200.00));
        response.setWarrantyStatus("1 year");
        response.setDistributorInfo("Distributor Inc.");
        response.setIsActive(true);
        response.setImageUrl("image.jpg");
        response.setCategory(category);

        assertEquals(1L, response.getId());
        assertEquals("Laptop", response.getName());
        assertEquals("Model X", response.getModel());
        assertEquals("12345", response.getSerialNumber());
        assertEquals("High-end laptop", response.getDescription());
        assertEquals(10, response.getQuantityInStock());
        assertEquals(BigDecimal.valueOf(1200.00), response.getPrice());
        assertEquals("1 year", response.getWarrantyStatus());
        assertEquals("Distributor Inc.", response.getDistributorInfo());
        assertTrue(response.getIsActive());
        assertEquals("image.jpg", response.getImageUrl());
        assertEquals(category, response.getCategory());
    }

    @Test
    void testToString() {
        CategoryResponse category = new CategoryResponse(1L, "Electronics", "Devices and gadgets");
        ProductResponse response = new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg", category);

        String expected = "ProductResponse [id=1, name=Laptop, model=Model X, serialNumber=12345, description=High-end laptop, quantityInStock=10, price=1200.0, warrantyStatus=1 year, distributorInfo=Distributor Inc., isActive=true, imageUrl=image.jpg, category=CategoryResponse [id=1, name=Electronics, description=Devices and gadgets]]";

        assertEquals(expected, response.toString());
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