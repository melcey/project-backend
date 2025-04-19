package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class LoginRequestTest {
    @Test
    void testLoginRequestCreation() {
        LoginRequest request = new LoginRequest("test@example.com", "password123", "customer");

        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        assertEquals("customer", request.getRole());
    }

    @Test
    void testSettersAndGetters() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
        loginRequest.setRole("customer");

        assertEquals("test@example.com", loginRequest.getEmail());
        assertEquals("password123", loginRequest.getPassword());
        assertEquals("customer", loginRequest.getRole());
    }

    @Test
    void testToString() {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123", "customer");
        String expected = "LoginRequest [email=test@example.com, password=password123, role=customer]";
        assertEquals(expected, loginRequest.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        LoginRequest login1 = new LoginRequest("test@example.com", "password123", "customer");
        LoginRequest login2 = new LoginRequest("test@example.com", "password123", "customer");
        LoginRequest login3 = new LoginRequest("admin@example.com", "adminpass", "admin");
        LoginRequest loginNull = new LoginRequest(null, null, null);

        // Test equality for objects with the same fields
        assertEquals(login1, login2);
        assertEquals(login1.hashCode(), login2.hashCode());

        // Test inequality for objects with different fields
        assertEquals(false, login1.equals(login3));
        assertEquals(false, login1.hashCode() == login3.hashCode());

        // Test equality for objects with all null fields
        LoginRequest loginNull2 = new LoginRequest(null, null, null);
        assertEquals(loginNull, loginNull2);
        assertEquals(loginNull.hashCode(), loginNull2.hashCode());

        // Test inequality with null
        assertEquals(false, login1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, login1.equals("someString"));

        // Test self-equality
        assertEquals(login1, login1);
    }
}