package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MessageResponseTest {
    @Test
    void testMessageResponseCreation() {
        MessageResponse response = new MessageResponse("Success");

        assertEquals("Success", response.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        MessageResponse messageResponse = new MessageResponse("Success");
        assertEquals("Success", messageResponse.getMessage());

        messageResponse.setMessage("Error");
        assertEquals("Error", messageResponse.getMessage());
    }

    @Test
    void testToString() {
        MessageResponse messageResponse = new MessageResponse("Success");
        String expected = "MessageResponse [message=Success]";
        assertEquals(expected, messageResponse.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        MessageResponse message1 = new MessageResponse("Success");
        MessageResponse message2 = new MessageResponse("Success");
        MessageResponse message3 = new MessageResponse("Error");
        MessageResponse messageNull = new MessageResponse(null);

        // Test equality for objects with the same message
        assertEquals(message1, message2);
        assertEquals(message1.hashCode(), message2.hashCode());

        // Test inequality for objects with different messages
        assertEquals(false, message1.equals(message3));
        assertEquals(false, message1.hashCode() == message3.hashCode());

        // Test equality for objects with null message
        MessageResponse messageNull2 = new MessageResponse(null);
        assertEquals(messageNull, messageNull2);
        assertEquals(messageNull.hashCode(), messageNull2.hashCode());

        // Test inequality with null
        assertEquals(false, message1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, message1.equals("someString"));

        // Test self-equality
        assertEquals(message1, message1);
    }
}