package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ProductListResponseTest {
    @Test
    void testProductListResponseCreation() {
        List<ProductResponse> products = new ArrayList<>();
        ProductListResponse response = new ProductListResponse(products);

        assertEquals(products, response.getProducts());
    }

    @Test
    void testSettersAndGetters() {
        List<ProductResponse> products = new ArrayList<>();
        ProductListResponse response = new ProductListResponse();
        response.setProducts(products);

        assertEquals(products, response.getProducts());
    }

    @Test
    void testToString() {
        List<ProductResponse> products = new ArrayList<>();
        ProductListResponse response = new ProductListResponse(products);
        String expected = "ProductListResponse [products=[]]";
        assertEquals(expected, response.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        List<ProductResponse> products1 = new ArrayList<>();
        products1.add(new ProductResponse(1L, "Laptop", "Model X", "12345", "High-end laptop", 10,
                BigDecimal.valueOf(1200.00), "1 year", "Distributor Inc.", true, "image.jpg",
                new CategoryResponse(1L, "Electronics", "Devices and gadgets")));

        List<ProductResponse> products2 = new ArrayList<>(products1);

        List<ProductResponse> products3 = new ArrayList<>();
        products3.add(new ProductResponse(2L, "Phone", "Model Y", "67890", "Smartphone", 5,
                BigDecimal.valueOf(800.00), "2 years", "Tech Distributors", false, "phone.jpg",
                new CategoryResponse(2L, "Mobiles", "Smart devices")));

        ProductListResponse response1 = new ProductListResponse(products1);
        ProductListResponse response2 = new ProductListResponse(products2);
        ProductListResponse response3 = new ProductListResponse(products3);
        ProductListResponse responseNull = new ProductListResponse(null);

        // Test equality for objects with the same products list
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        // Test inequality for objects with different products lists
        assertEquals(false, response1.equals(response3));
        assertEquals(false, response1.hashCode() == response3.hashCode());

        // Test equality for objects with null products list
        ProductListResponse responseNull2 = new ProductListResponse(null);
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