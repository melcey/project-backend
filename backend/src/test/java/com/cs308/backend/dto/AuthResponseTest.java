package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AuthResponseTest {
    @Test
    void testAuthResponseCreation() {
        AuthResponse authResponse = new AuthResponse("testAccessToken");

        assertEquals("testAccessToken", authResponse.getAccessToken());
        assertEquals("Bearer", authResponse.getTokenType());
    }

    @Test
    void testSettersAndGetters() {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken("testToken");
        authResponse.setTokenType("Bearer");

        assertEquals("testToken", authResponse.getAccessToken());
        assertEquals("Bearer", authResponse.getTokenType());
    }

    @Test
    void testToString() {
        AuthResponse authResponse = new AuthResponse("testToken");
        String expected = "AuthResponse [accessToken=testToken, tokenType=Bearer]";
        assertEquals(expected, authResponse.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        AuthResponse authResponse1 = new AuthResponse("testToken");
        AuthResponse authResponse2 = new AuthResponse("testToken");
        AuthResponse authResponse3 = new AuthResponse("differentToken");
        AuthResponse authResponseNull = new AuthResponse(null);

        // Test equality for objects with the same accessToken
        assertEquals(authResponse1, authResponse2);
        assertEquals(authResponse1.hashCode(), authResponse2.hashCode());

        // Test inequality for objects with different accessToken
        assertEquals(false, authResponse1.equals(authResponse3));
        assertEquals(false, authResponse1.hashCode() == authResponse3.hashCode());

        // Test equality for objects with null accessToken
        AuthResponse authResponseNull2 = new AuthResponse(null);
        assertEquals(authResponseNull, authResponseNull2);
        assertEquals(authResponseNull.hashCode(), authResponseNull2.hashCode());

        // Test inequality with null
        assertEquals(false, authResponse1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, authResponse1.equals("someString"));

        // Test self-equality
        assertEquals(authResponse1, authResponse1);
    }
}