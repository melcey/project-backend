package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class AnonCartItemTest {
    @Test
    void testAnonCartItemCreation() {
        AnonCart cart = new AnonCart();
        Product product = new Product();
        AnonCartItem cartItem = new AnonCartItem(cart, product, 2, BigDecimal.valueOf(50.25));

        assertEquals(cart, cartItem.getCart());
        assertEquals(product, cartItem.getProduct());
        assertEquals(2, cartItem.getQuantity());
        assertEquals(BigDecimal.valueOf(50.25), cartItem.getPriceAtAddition());
        assertNotNull(cartItem.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        AnonCart cart = new AnonCart();
        Product product = new Product();
        AnonCartItem cartItem = new AnonCartItem();

        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(3);
        cartItem.setPriceAtAddition(BigDecimal.valueOf(75.50));

        assertEquals(1L, cartItem.getId());
        assertEquals(cart, cartItem.getCart());
        assertEquals(product, cartItem.getProduct());
        assertEquals(3, cartItem.getQuantity());
        assertEquals(BigDecimal.valueOf(75.50), cartItem.getPriceAtAddition());
    }

    @Test
    void testToString() {
        AnonCart cart = new AnonCart();
        Product product = new Product();
        product.setName("Laptop");
        AnonCartItem cartItem = new AnonCartItem(cart, product, 2, BigDecimal.valueOf(50.25));
        LocalDateTime createdAt = LocalDateTime.of(2025, 4, 19, 12, 0);
        cartItem.setCreatedAt(createdAt);
        cart.setCreatedAt(LocalDateTime.of(2025, 4, 19, 11, 0));
        cart.setUpdatedAt(LocalDateTime.of(2025, 4, 19, 13, 0));

        String expected = "AnonCartItem [id=null, cart=AnonCart [id=null, totalPrice=0, items=[], createdAt=2025-04-19T11:00, updatedAt=2025-04-19T13:00], product=Product [id=null, name=Laptop, model=null, serialNumber=null, description=null, quantityInStock=0, price=null, warrantyStatus=null, distributorInfo=null, isActive=true, imageUrl=null, isPriced=false, originalPrice=null, discountRate=0, costPrice=null, category=null, productManager=null], quantity=2, priceAtAddition=50.25, createdAt=2025-04-19T12:00]";

        assertEquals(expected, cartItem.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {
        AnonCart cart1 = new AnonCart();
        AnonCart cart2 = new AnonCart();
        Product product1 = new Product();
        Product product2 = new Product();

        AnonCartItem cartItem1 = new AnonCartItem(cart1, product1, 2, BigDecimal.valueOf(50.25));
        AnonCartItem cartItem2 = new AnonCartItem(cart1, product1, 2, BigDecimal.valueOf(50.25));
        AnonCartItem cartItem3 = new AnonCartItem(cart2, product2, 3, BigDecimal.valueOf(75.50));
        AnonCartItem cartItemNull = new AnonCartItem();

        // Test equality for objects with the same fields
        cartItem1.setId(1L);
        cartItem2.setId(1L);
        assertEquals(cartItem1, cartItem2);
        assertEquals(cartItem1.hashCode(), cartItem2.hashCode());

        // Test inequality for objects with different fields
        cartItem3.setId(2L);
        assertEquals(false, cartItem1.equals(cartItem3));
        assertEquals(false, cartItem1.hashCode() == cartItem3.hashCode());

        // Test equality for objects with null fields
        AnonCartItem cartItemNull2 = new AnonCartItem();
        assertEquals(cartItemNull, cartItemNull2);
        assertEquals(cartItemNull.hashCode(), cartItemNull2.hashCode());

        // Test inequality with null
        assertEquals(false, cartItem1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, cartItem1.equals("someString"));

        // Test self-equality
        assertEquals(cartItem1, cartItem1);
    }
}