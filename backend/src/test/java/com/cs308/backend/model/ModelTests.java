package com.cs308.backend.model;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ModelTests {

    @Test
    void testUserComplete() {
        // Create a user and test setters/getters
        User user1 = new User();
        user1.setName("John Doe");
        user1.setAddress("123 Main St");
        user1.setRole(Role.product_manager);
        byte[] emailBytes = "encryptedEmail".getBytes();
        byte[] passwordBytes = "hashedPassword".getBytes();
        user1.setEncryptedEmail(emailBytes);
        user1.setPasswordHashed(passwordBytes);
        user1.setId(1L);
        
        // Verify getters
        Assertions.assertEquals("John Doe", user1.getName());
        Assertions.assertEquals("123 Main St", user1.getAddress());
        Assertions.assertEquals(Role.product_manager, user1.getRole());
        Assertions.assertArrayEquals(emailBytes, user1.getEncryptedEmail());
        Assertions.assertArrayEquals(passwordBytes, user1.getPasswordHashed());
        Assertions.assertNotNull(user1.toString());
        
        // Test equals and hashCode by creating another identical user
        User user2 = new User();
        user2.setName("John Doe");
        user2.setAddress("123 Main St");
        user2.setRole(Role.product_manager);
        user2.setEncryptedEmail("encryptedEmail".getBytes());
        user2.setPasswordHashed("hashedPassword".getBytes());
        user2.setId(1L);
        
        Assertions.assertEquals(user1, user2);
        Assertions.assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testCategoryComplete() {
        // Create a category and test setters/getters
        Category cat1 = new Category();
        cat1.setName("Electronics");
        cat1.setDescription("Electronic devices and gadgets");
        cat1.setId(10L);
        
        Assertions.assertEquals("Electronics", cat1.getName());
        Assertions.assertEquals("Electronic devices and gadgets", cat1.getDescription());
        Assertions.assertNotNull(cat1.toString());
        
        // Create another identical category and test equals/hashCode
        Category cat2 = new Category();
        cat2.setName("Electronics");
        cat2.setDescription("Electronic devices and gadgets");
        cat2.setId(10L);
        
        Assertions.assertEquals(cat1, cat2);
        Assertions.assertEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testProductComplete() {
        // Create dependencies
        Category category = new Category();
        category.setName("Books");
        category.setDescription("Books category");
        category.setId(100L);
        
        User manager = new User();
        manager.setName("Manager One");
        manager.setAddress("Manager Address");
        manager.setRole(Role.product_manager);
        manager.setId(200L);
        
        // Create a product and test setters/getters
        Product prod1 = new Product();
        prod1.setName("Effective Java");
        prod1.setModel("3rd Edition");
        prod1.setSerialNumber("EJ-003");
        prod1.setDescription("A guide to writing good Java code");
        prod1.setPrice(new BigDecimal("45.00"));
        prod1.setQuantityInStock(15);
        prod1.setCategory(category);
        prod1.setProductManager(manager);
        prod1.setId(500L);
        
        // Verify getters
        Assertions.assertEquals("Effective Java", prod1.getName());
        Assertions.assertEquals("3rd Edition", prod1.getModel());
        Assertions.assertEquals("EJ-003", prod1.getSerialNumber());
        Assertions.assertEquals("A guide to writing good Java code", prod1.getDescription());
        Assertions.assertEquals(new BigDecimal("45.00"), prod1.getPrice());
        Assertions.assertEquals(15, prod1.getQuantityInStock());
        Assertions.assertEquals(category, prod1.getCategory());
        Assertions.assertEquals(manager, prod1.getProductManager());
        Assertions.assertNotNull(prod1.toString());
        
        // Test equals and hashCode by creating an identical product
        Product prod2 = new Product();
        prod2.setName("Effective Java");
        prod2.setModel("3rd Edition");
        prod2.setSerialNumber("EJ-003");
        prod2.setDescription("A guide to writing good Java code");
        prod2.setPrice(new BigDecimal("45.00"));
        prod2.setQuantityInStock(15);
        prod2.setCategory(category);
        prod2.setProductManager(manager);
        prod2.setId(500L);
        
        Assertions.assertEquals(prod1, prod2);
        Assertions.assertEquals(prod1.hashCode(), prod2.hashCode());
    }
}