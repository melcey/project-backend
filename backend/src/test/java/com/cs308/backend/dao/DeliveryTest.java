package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class DeliveryTest {
    @Test
    void testDeliveryCreation() {
        Order order = new Order();
        Product product = new Product();
        Delivery delivery = new Delivery(order, product, 5, BigDecimal.valueOf(250.75), "123 Street", "Pending");

        assertEquals(order, delivery.getOrder());
        assertEquals(product, delivery.getProduct());
        assertEquals(5, delivery.getQuantity());
        assertEquals(BigDecimal.valueOf(250.75), delivery.getTotalPrice());
        assertEquals("123 Street", delivery.getDeliveryAddress());
        assertEquals("Pending", delivery.getDeliveryStatus());
    }

    @Test
    void testSettersAndGetters() {
        Order order = new Order();
        Product product = new Product();
        Delivery delivery = new Delivery();

        delivery.setId(1L);
        delivery.setOrder(order);
        delivery.setProduct(product);
        delivery.setQuantity(5);
        delivery.setTotalPrice(BigDecimal.valueOf(250.75));
        delivery.setDeliveryAddress("123 Street");
        delivery.setDeliveryStatus("Pending");

        assertEquals(1L, delivery.getId());
        assertEquals(order, delivery.getOrder());
        assertEquals(product, delivery.getProduct());
        assertEquals(5, delivery.getQuantity());
        assertEquals(BigDecimal.valueOf(250.75), delivery.getTotalPrice());
        assertEquals("123 Street", delivery.getDeliveryAddress());
        assertEquals("Pending", delivery.getDeliveryStatus());
    }

    @Test
    void testToString() {
        Order order = new Order();
        Product product = new Product();
        product.setName("Laptop");
        Delivery delivery = new Delivery(order, product, 5, BigDecimal.valueOf(250.75), "123 Street", "Pending");

        String expected = "Delivery [id=null, order=Order [id=null, user=null, orderDate=null, status=null, totalPrice=null, deliveryAddress=null, orderItems=[]], product=Product [id=null, name=Laptop, model=null, serialNumber=null, description=null, quantityInStock=0, price=null, warrantyStatus=null, distributorInfo=null, isActive=true, imageUrl=null, category=null, productManager=null], quantity=5, totalPrice=250.75, deliveryAddress=123 Street, deliveryStatus=Pending]";
        assertEquals(expected, delivery.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {
        Order order1 = new Order();
        Order order2 = new Order();
        Product product1 = new Product();
        Product product2 = new Product();

        Delivery delivery1 = new Delivery(order1, product1, 5, BigDecimal.valueOf(250.75), "123 Street", "Pending");
        Delivery delivery2 = new Delivery(order1, product1, 5, BigDecimal.valueOf(250.75), "123 Street", "Pending");
        Delivery delivery3 = new Delivery(order2, product2, 10, BigDecimal.valueOf(500.00), "456 Avenue", "Shipped");
        Delivery deliveryNull = new Delivery();

        // Test equality for objects with the same fields
        delivery1.setId(1L);
        delivery2.setId(1L);
        assertEquals(delivery1, delivery2);
        assertEquals(delivery1.hashCode(), delivery2.hashCode());

        // Test inequality for objects with different fields
        delivery3.setId(2L);
        assertEquals(false, delivery1.equals(delivery3));
        assertEquals(false, delivery1.hashCode() == delivery3.hashCode());

        // Test equality for objects with null fields
        Delivery deliveryNull2 = new Delivery();
        assertEquals(deliveryNull, deliveryNull2);
        assertEquals(deliveryNull.hashCode(), deliveryNull2.hashCode());

        // Test inequality with null
        assertEquals(false, delivery1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, delivery1.equals("someString"));

        // Test self-equality
        assertEquals(delivery1, delivery1);
    }
}