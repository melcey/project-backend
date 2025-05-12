package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class ProductTest {
    @Test
    void testProductCreation() {
        Product product = new Product("Laptop", "Model X", "12345", "High-end laptop", 10, BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg");

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
    }
    
    @Test
    void testSettersAndGetters() {
        Product product = new Product();
        Category category = new Category();

        product.setId(1L);
        product.setName("Laptop");
        product.setModel("Model X");
        product.setSerialNumber("12345");
        product.setDescription("High-end laptop");
        product.setQuantityInStock(10);
        product.setPrice(BigDecimal.valueOf(1200.00));
        product.setWarrantyStatus("1 year");
        product.setDistributorInfo("Distributor Inc.");
        product.setIsActive(true);
        product.setImageUrl("image.jpg");
        product.setCategory(category);

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
    void testToString() {
        Product product = new Product("Laptop", "Model X", "12345", "High-end laptop", 10, BigDecimal.valueOf(1200.00),
                "1 year", "Distributor Inc.", true, "image.jpg");

        String expected = "Product [id=null, name=Laptop, model=Model X, serialNumber=12345, description=High-end laptop, quantityInStock=10, price=1200.0, warrantyStatus=1 year, distributorInfo=Distributor Inc., isActive=true, imageUrl=image.jpg, isPriced=false, originalPrice=1200.0, discountRate=0, costPrice=600.0, category=null, productManager=null]";
        assertEquals(expected, product.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {
        Category category1 = new Category("Electronics", "Devices and gadgets");
        Category category2 = new Category("Books", "All kinds of books");
        User productManager1 = new User();
        User productManager2 = new User();

        Product product1 = new Product("Laptop", "Model X", "12345", "High-end laptop", 10, BigDecimal.valueOf(1200.00),
                "1 year", "Distributor Inc.", true, "image.jpg");
        product1.setCategory(category1);
        product1.setProductManager(productManager1);

        Product product2 = new Product("Laptop", "Model X", "12345", "High-end laptop", 10, BigDecimal.valueOf(1200.00),
                "1 year", "Distributor Inc.", true, "image.jpg");
        product2.setCategory(category1);
        product2.setProductManager(productManager1);

        Product product3 = new Product("Phone", "Model Y", "67890", "Smartphone", 5, BigDecimal.valueOf(800.00),
                "2 years", "Tech Distributors", false, "phone.jpg");
        product3.setCategory(category2);
        product3.setProductManager(productManager2);

        Product productNull = new Product();

        // Test equality for objects with the same fields
        product1.setId(1L);
        product2.setId(1L);
        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());

        // Test inequality for objects with different fields
        product3.setId(2L);
        assertEquals(false, product1.equals(product3));
        assertEquals(false, product1.hashCode() == product3.hashCode());

        // Test equality for objects with null fields
        Product productNull2 = new Product();
        assertEquals(productNull, productNull2);
        assertEquals(productNull.hashCode(), productNull2.hashCode());

        // Test inequality with null
        assertEquals(false, product1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, product1.equals("someString"));

        // Test self-equality
        assertEquals(product1, product1);
    }
}