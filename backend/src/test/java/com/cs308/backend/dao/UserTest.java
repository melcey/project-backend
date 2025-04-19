package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void testUserCreationWithAllFields() {
        byte[] email = "encryptedEmail".getBytes();
        byte[] password = "hashedPassword".getBytes();
        User user = new User(1L, "John Doe", email, "123 Street", password, Role.customer);

        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertArrayEquals(email, user.getEncryptedEmail());
        assertEquals("123 Street", user.getAddress());
        assertArrayEquals(password, user.getPasswordHashed());
        assertEquals(Role.customer, user.getRole());
        assertNotNull(user.getManagedProducts());
    }

    @Test
    void testUserCreationWithMinimalFields() {
        User user = new User("Jane Doe", "456 Avenue", Role.product_manager);

        assertNull(user.getId());
        assertEquals("Jane Doe", user.getName());
        assertNull(user.getEncryptedEmail());
        assertEquals("456 Avenue", user.getAddress());
        assertNull(user.getPasswordHashed());
        assertEquals(Role.product_manager, user.getRole());
        assertNotNull(user.getManagedProducts());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        user.setId(2L);
        user.setName("Alice");
        user.setEncryptedEmail("encryptedEmail".getBytes());
        user.setAddress("789 Boulevard");
        user.setPasswordHashed("hashedPassword".getBytes());
        user.setRole(Role.sales_manager);
        user.setManagedProducts(new HashSet<>());

        assertEquals(2L, user.getId());
        assertEquals("Alice", user.getName());
        assertArrayEquals("encryptedEmail".getBytes(), user.getEncryptedEmail());
        assertEquals("789 Boulevard", user.getAddress());
        assertArrayEquals("hashedPassword".getBytes(), user.getPasswordHashed());
        assertEquals(Role.sales_manager, user.getRole());
        assertNotNull(user.getManagedProducts());
    }

    @Test
    void testToString() {
        User user = new User("John Doe", "123 Street", Role.customer);

        String expected = "User [id=null, name=John Doe, address=123 Street, role=customer]";
        assertEquals(expected, user.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {
        byte[] email1 = "encryptedEmail1".getBytes();
        byte[] email2 = "encryptedEmail2".getBytes();
        byte[] password1 = "hashedPassword1".getBytes();
        byte[] password2 = "hashedPassword2".getBytes();

        User user1 = new User(1L, "John Doe", email1, "123 Street", password1, Role.customer);
        User user2 = new User(1L, "John Doe", email1, "123 Street", password1, Role.customer);
        User user3 = new User(2L, "Jane Doe", email2, "456 Avenue", password2, Role.product_manager);
        User userNull = new User();

        // Test equality for objects with the same fields
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());

        // Test inequality for objects with different fields
        assertEquals(false, user1.equals(user3));
        assertEquals(false, user1.hashCode() == user3.hashCode());

        // Test equality for objects with null fields
        User userNull2 = new User();
        assertEquals(userNull, userNull2);
        assertEquals(userNull.hashCode(), userNull2.hashCode());

        // Test inequality with null
        assertEquals(false, user1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, user1.equals("someString"));

        // Test self-equality
        assertEquals(user1, user1);
    }
}