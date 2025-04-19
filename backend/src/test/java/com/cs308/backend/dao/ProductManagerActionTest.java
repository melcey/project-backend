package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ProductManagerActionTest {
    @Test
    void testProductManagerActionCreation() {
        User productManager = new User();
        String actionType = "ADD_PRODUCT";
        String details = "Added a new product to the catalog";

        ProductManagerAction action = new ProductManagerAction(productManager, actionType, details);

        assertEquals(productManager, action.getProductManager());
        assertEquals(actionType, action.getActionType());
        assertEquals(details, action.getDetails());
        assertNotNull(action.getActionDate());
    }

    @Test
    void testSettersAndGetters() {
        ProductManagerAction action = new ProductManagerAction();
        User productManager = new User();
        action.setProductManager(productManager);
        action.setActionType("UPDATE_PRODUCT");
        action.setDetails("Updated product details");
        action.setActionDate(LocalDateTime.now());

        assertEquals(productManager, action.getProductManager());
        assertEquals("UPDATE_PRODUCT", action.getActionType());
        assertEquals("Updated product details", action.getDetails());
        assertNotNull(action.getActionDate());
    }

    @Test
    void testToString() {
        User productManager = new User();
        productManager.setId(1L);
        productManager.setName("testUser");

        String actionType = "DELETE_PRODUCT";
        String details = "Deleted a product from the catalog";

        LocalDateTime fixedActionDate = LocalDateTime.of(2025, 4, 19, 12, 0);
        ProductManagerAction action = new ProductManagerAction(productManager, actionType, details);
        action.setActionId(1L);
        action.setActionDate(fixedActionDate);

        StringBuilder expectedStringBuilder = new StringBuilder();
        expectedStringBuilder.append("ProductManagerAction [actionId=1, productManager=")
            .append(productManager)
            .append(", actionType=DELETE_PRODUCT, actionDate=")
            .append(fixedActionDate)
            .append(", details=Deleted a product from the catalog]");

        String expectedString = expectedStringBuilder.toString();

        assertEquals(expectedString, action.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {
        User productManager1 = new User();
        User productManager2 = new User();

        ProductManagerAction action1 = new ProductManagerAction(productManager1, "ADD_PRODUCT", "Added a new product");
        ProductManagerAction action2 = new ProductManagerAction(productManager1, "ADD_PRODUCT", "Added a new product");
        ProductManagerAction action3 = new ProductManagerAction(productManager2, "DELETE_PRODUCT", "Deleted a product");
        ProductManagerAction actionNull = new ProductManagerAction();

        // Test equality for objects with the same fields
        action1.setActionId(1L);
        action2.setActionId(1L);
        assertEquals(action1, action2);
        assertEquals(action1.hashCode(), action2.hashCode());

        // Test inequality for objects with different fields
        action3.setActionId(2L);
        assertEquals(false, action1.equals(action3));
        assertEquals(false, action1.hashCode() == action3.hashCode());

        // Test equality for objects with null fields
        ProductManagerAction actionNull2 = new ProductManagerAction();
        assertEquals(actionNull, actionNull2);
        assertEquals(actionNull.hashCode(), actionNull2.hashCode());

        // Test inequality with null
        assertEquals(false, action1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, action1.equals("someString"));

        // Test self-equality
        assertEquals(action1, action1);
    }
}