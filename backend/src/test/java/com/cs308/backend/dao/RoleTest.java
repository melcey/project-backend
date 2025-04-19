package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class RoleTest {
    @Test
    void testRoleFromString() {
        assertEquals(Role.customer, Role.fromString("customer"));
        assertEquals(Role.sales_manager, Role.fromString("sales_manager"));
        assertEquals(Role.product_manager, Role.fromString("product_manager"));
    }

    @Test
    void testInvalidRoleFromString() {
        assertThrows(IllegalArgumentException.class, () -> Role.fromString("invalid_role"));
    }

    @Test
    void testSetRole() {
        User user = new User();
        user.setRole(Role.sales_manager);

        assertEquals(Role.sales_manager, user.getRole());
    }

    @Test
    void testRoleToString() {
        assertEquals("customer", Role.customer.toString());
        assertEquals("sales_manager", Role.sales_manager.toString());
        assertEquals("product_manager", Role.product_manager.toString());
    }

    @Test
    void testHashCodeAndEquals() {
        // Test equality for the same enum value
        assertEquals(Role.customer, Role.fromString("customer"));
        assertEquals(Role.customer.hashCode(), Role.fromString("customer").hashCode());

        // Test inequality for different enum values
        assertNotEquals(Role.customer, Role.sales_manager);
        assertNotEquals(Role.customer.hashCode(), Role.sales_manager.hashCode());

        // Test self-equality
        assertEquals(Role.product_manager, Role.product_manager);

        // Test inequality with null
        assertNotEquals(Role.customer, null);

        // Test inequality with an object of a different class
        assertNotEquals(Role.sales_manager, "sales_manager");
    }
}