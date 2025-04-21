package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class AnonCartTest {
    @Test
    void testAnonCartCreation() {
        List<AnonCartItem> items = new ArrayList<>();
        AnonCart cart = new AnonCart(BigDecimal.valueOf(100.50), items);

        assertEquals(BigDecimal.valueOf(100.50), cart.getTotalPrice());
        assertEquals(items, cart.getItems());
        assertNotNull(cart.getCreatedAt());
        assertNotNull(cart.getUpdatedAt());
    }

    @Test
    void testAddItemsToAnonCart() {
        AnonCart cart = new AnonCart();
        AnonCartItem item1 = new AnonCartItem();
        AnonCartItem item2 = new AnonCartItem();

        List<AnonCartItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        cart.setItems(items);

        assertEquals(2, cart.getItems().size());
        assertTrue(cart.getItems().contains(item1));
        assertTrue(cart.getItems().contains(item2));
    }

    @Test
    void testSettersAndGetters() {
        List<AnonCartItem> items = new ArrayList<>();
        AnonCart cart = new AnonCart();

        cart.setId(1L);
        cart.setTotalPrice(BigDecimal.valueOf(200.00));
        cart.setItems(items);

        assertEquals(1L, cart.getId());
        assertEquals(BigDecimal.valueOf(200.00), cart.getTotalPrice());
        assertEquals(items, cart.getItems());
    }

    @Test
    void testToString() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 4, 19, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 4, 20, 12, 0);
        AnonCart cart = new AnonCart(BigDecimal.valueOf(100.50), new ArrayList<>());
        cart.setCreatedAt(createdAt);
        cart.setUpdatedAt(updatedAt);

        String expected = "AnonCart [id=null, totalPrice=100.5, items=[], createdAt=2025-04-19T12:00, updatedAt=2025-04-20T12:00]";
        assertEquals(expected, cart.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {

        AnonCart cart1 = new AnonCart(BigDecimal.valueOf(100.50), new ArrayList<>());
        AnonCart cart2 = new AnonCart(BigDecimal.valueOf(100.50), new ArrayList<>());
        AnonCart cart3 = new AnonCart(BigDecimal.valueOf(200.00), new ArrayList<>());
        AnonCart cartNull = new AnonCart();

        // Test equality for objects with the same fields
        cart1.setId(1L);
        cart2.setId(1L);
        assertEquals(cart1, cart2);
        assertEquals(cart1.hashCode(), cart2.hashCode());

        // Test inequality for objects with different fields
        cart3.setId(2L);
        assertEquals(false, cart1.equals(cart3));
        assertEquals(false, cart1.hashCode() == cart3.hashCode());

        // Test equality for objects with null fields
        AnonCart cartNull2 = new AnonCart();
        assertEquals(cartNull, cartNull2);
        assertEquals(cartNull.hashCode(), cartNull2.hashCode());

        // Test inequality with null
        assertEquals(false, cart1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, cart1.equals("someString"));

        // Test self-equality
        assertEquals(cart1, cart1);
    }
}