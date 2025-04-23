package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class CartTest {
    @Test
    void testCartCreation() {
        User user = new User();
        List<CartItem> items = new ArrayList<>();
        Cart cart = new Cart(user, BigDecimal.valueOf(100.50), items);

        assertEquals(user, cart.getUser());
        assertEquals(BigDecimal.valueOf(100.50), cart.getTotalPrice());
        assertEquals(items, cart.getItems());
        assertNotNull(cart.getCreatedAt());
        assertNotNull(cart.getUpdatedAt());
    }

    @Test
    void testAddItemsToCart() {
        Cart cart = new Cart();
        CartItem item1 = new CartItem();
        CartItem item2 = new CartItem();

        List<CartItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        cart.setItems(items);

        assertEquals(2, cart.getItems().size());
        assertTrue(cart.getItems().contains(item1));
        assertTrue(cart.getItems().contains(item2));
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        List<CartItem> items = new ArrayList<>();
        Cart cart = new Cart();

        cart.setId(1L);
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.valueOf(200.00));
        cart.setItems(items);

        assertEquals(1L, cart.getId());
        assertEquals(user, cart.getUser());
        assertEquals(BigDecimal.valueOf(200.00), cart.getTotalPrice());
        assertEquals(items, cart.getItems());
    }

    @Test
    void testToString() {
        User user = new User();
        user.setName("John Doe");
        user.setRole(Role.customer);
        LocalDateTime createdAt = LocalDateTime.of(2025, 4, 19, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 4, 20, 12, 0);
        Cart cart = new Cart(user, BigDecimal.valueOf(100.50), new ArrayList<>());
        cart.setCreatedAt(createdAt);
        cart.setUpdatedAt(updatedAt);

        String expected = "Cart [id=null, user=User [id=null, name=John Doe, address=null, role=customer], totalPrice=100.5, items=[], createdAt=2025-04-19T12:00, updatedAt=2025-04-20T12:00]";
        assertEquals(expected, cart.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {
        User user1 = new User();
        User user2 = new User();

        Cart cart1 = new Cart(user1, BigDecimal.valueOf(100.50), new ArrayList<>());
        Cart cart2 = new Cart(user1, BigDecimal.valueOf(100.50), new ArrayList<>());
        Cart cart3 = new Cart(user2, BigDecimal.valueOf(200.00), new ArrayList<>());
        Cart cartNull = new Cart();

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
        Cart cartNull2 = new Cart();
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