package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class OrderItemTest {
    @Test
    void testOrderItemCreation() {
        Order order = new Order();
        Product product = new Product();
        OrderItem orderItem = new OrderItem(order, product, 3, BigDecimal.valueOf(150.00));

        assertEquals(order, orderItem.getOrder());
        assertEquals(product, orderItem.getProduct());
        assertEquals(3, orderItem.getQuantity());
        assertEquals(BigDecimal.valueOf(150.00), orderItem.getPrice());
    }

    @Test
    void testSettersAndGetters() {
        Order order = new Order();
        Product product = new Product();
        OrderItem orderItem = new OrderItem();

        orderItem.setId(1L);
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(3);
        orderItem.setPrice(BigDecimal.valueOf(150.00));

        assertEquals(1L, orderItem.getId());
        assertEquals(order, orderItem.getOrder());
        assertEquals(product, orderItem.getProduct());
        assertEquals(3, orderItem.getQuantity());
        assertEquals(BigDecimal.valueOf(150.00), orderItem.getPrice());
    }

    @Test
    void testToString() {
        Order order = new Order();
        Product product = new Product();
        product.setName("Laptop");
        OrderItem orderItem = new OrderItem(order, product, 3, BigDecimal.valueOf(150.00));

        String expected = "OrderItem [id=null, order=Order [id=null, user=null, orderDate=null, status=null, totalPrice=null, deliveryAddress=null, orderItems=[]], product=Product [id=null, name=Laptop, model=null, serialNumber=null, description=null, quantityInStock=0, price=null, warrantyStatus=null, distributorInfo=null, isActive=true, imageUrl=null, isPriced=false, originalPrice=null, discountRate=0, costPrice=null, category=null, productManager=null], quantity=3, price=150.0]";
        assertEquals(expected, orderItem.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {
        Order order1 = new Order();
        Order order2 = new Order();
        Product product1 = new Product();
        Product product2 = new Product();

        OrderItem orderItem1 = new OrderItem(order1, product1, 3, BigDecimal.valueOf(150.00));
        OrderItem orderItem2 = new OrderItem(order1, product1, 3, BigDecimal.valueOf(150.00));
        OrderItem orderItem3 = new OrderItem(order2, product2, 5, BigDecimal.valueOf(250.00));
        OrderItem orderItemNull = new OrderItem();

        // Test equality for objects with the same fields
        orderItem1.setId(1L);
        orderItem2.setId(1L);
        assertEquals(orderItem1, orderItem2);
        assertEquals(orderItem1.hashCode(), orderItem2.hashCode());

        // Test inequality for objects with different fields
        orderItem3.setId(2L);
        assertEquals(false, orderItem1.equals(orderItem3));
        assertEquals(false, orderItem1.hashCode() == orderItem3.hashCode());

        // Test equality for objects with null fields
        OrderItem orderItemNull2 = new OrderItem();
        assertEquals(orderItemNull, orderItemNull2);
        assertEquals(orderItemNull.hashCode(), orderItemNull2.hashCode());

        // Test inequality with null
        assertEquals(false, orderItem1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, orderItem1.equals("someString"));

        // Test self-equality
        assertEquals(orderItem1, orderItem1);
    }
}