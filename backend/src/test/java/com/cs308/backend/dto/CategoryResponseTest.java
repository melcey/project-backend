package com.cs308.backend.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CategoryResponseTest {
    @Test
    void testCategoryResponseCreation() {
        CategoryResponse category = new CategoryResponse(1L, "Electronics", "Devices and gadgets");

        assertEquals(1L, category.getId());
        assertEquals("Electronics", category.getName());
        assertEquals("Devices and gadgets", category.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setName("Electronics");
        categoryResponse.setDescription("Devices and gadgets");

        assertEquals(1L, categoryResponse.getId());
        assertEquals("Electronics", categoryResponse.getName());
        assertEquals("Devices and gadgets", categoryResponse.getDescription());
    }

    @Test
    void testToString() {
        CategoryResponse category = new CategoryResponse(1L, "Electronics", "Devices and gadgets");
        String expected = "CategoryResponse [id=1, name=Electronics, description=Devices and gadgets]";
        assertEquals(expected, category.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEqualsAndHashCode() {
        CategoryResponse category1 = new CategoryResponse(1L, "Electronics", "Devices and gadgets");
        CategoryResponse category2 = new CategoryResponse(1L, "Electronics", "Devices and gadgets");
        CategoryResponse category3 = new CategoryResponse(2L, "Books", "Fiction and non-fiction");
        CategoryResponse categoryNull = new CategoryResponse(null, null, null);

        // Test equality for objects with the same id, name, and description
        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());

        // Test inequality for objects with different id, name, or description
        assertEquals(false, category1.equals(category3));
        assertEquals(false, category1.hashCode() == category3.hashCode());

        // Test equality for objects with all null fields
        CategoryResponse categoryNull2 = new CategoryResponse(null, null, null);
        assertEquals(categoryNull, categoryNull2);
        assertEquals(categoryNull.hashCode(), categoryNull2.hashCode());

        // Test inequality with null
        assertEquals(false, category1.equals(null));

        // Test inequality with an object of a different class
        assertEquals(false, category1.equals("someString"));

        // Test self-equality
        assertEquals(category1, category1);
    }
}