package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class OrderItemResponseTest {
    @Test
    void testOrderItemResponseCreation() {
        CategoryResponse category = new CategoryResponse(1L, "Electronics", "Devices and gadgets");
        ProductResponse product = new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg", category);

        OrderItemResponse orderItem = new OrderItemResponse(1L, 1L, product, 2, BigDecimal.valueOf(100.00));

        assertEquals(1L, orderItem.getId());
        assertEquals(1L, orderItem.getOrderId());
        assertEquals(product, orderItem.getProduct());
        assertEquals(2, orderItem.getQuantity());
        assertEquals(BigDecimal.valueOf(100.00), orderItem.getPrice());
    }

    @Test
    void testSettersAndGetters() {
        ProductResponse product = new ProductResponse();
        OrderItemResponse response = new OrderItemResponse();
        response.setId(1L);
        response.setOrderId(2L);
        response.setProduct(product);
        response.setQuantity(3);
        response.setPrice(BigDecimal.valueOf(150.00));

        assertEquals(1L, response.getId());
        assertEquals(2L, response.getOrderId());
        assertEquals(product, response.getProduct());
        assertEquals(3, response.getQuantity());
        assertEquals(BigDecimal.valueOf(150.00), response.getPrice());
    }

    @Test
    void testToString() {
        ProductResponse product = new ProductResponse();
        OrderItemResponse response = new OrderItemResponse(1L, 2L, product, 3, BigDecimal.valueOf(150.00));
        String expected = "OrderItemResponse [id=1, orderId=2, product=ProductResponse [id=null, name=null, model=null, serialNumber=null, description=null, quantityInStock=0, price=null, warrantyStatus=null, distributorInfo=null, isActive=false, imageUrl=null, category=CategoryResponse [id=null, name=null, description=null]], quantity=3, price=150.0]";
        assertEquals(expected, response.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        ProductResponse product1 = new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg",
                new CategoryResponse(1L, "Electronics", "Devices and gadgets"));
        ProductResponse product2 = new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg",
                new CategoryResponse(1L, "Electronics", "Devices and gadgets"));
        ProductResponse product3 = new ProductResponse(2L, "Phone", "Model Y", "67890", "Smartphone", 5,
                BigDecimal.valueOf(800.00), "2 years", "Tech Distributors", false, "phone.jpg",
                new CategoryResponse(2L, "Mobiles", "Smart devices"));

        OrderItemResponse response1 = new OrderItemResponse(1L, 2L, product1, 3, BigDecimal.valueOf(150.00));
        OrderItemResponse response2 = new OrderItemResponse(1L, 2L, product2, 3, BigDecimal.valueOf(150.00));
        OrderItemResponse response3 = new OrderItemResponse(2L, 3L, product3, 5, BigDecimal.valueOf(200.00));
        OrderItemResponse responseNull = new OrderItemResponse(null, null, null, null, null);

        // Test equality for objects with the same fields
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        // Test inequality for objects with different fields
        assertEquals(false, response1.equals(response3));
        assertEquals(false, response1.hashCode() == response3.hashCode());

        // Test equality for objects with all null fields
        OrderItemResponse responseNull2 = new OrderItemResponse(null, null, null, null, null);
        assertEquals(responseNull, responseNull2);
        assertEquals(responseNull.hashCode(), responseNull2.hashCode());

        // Test inequality with null
        assertEquals(false, response1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, response1.equals("someString"));

        // Test self-equality
        assertEquals(response1, response1);
    }
}