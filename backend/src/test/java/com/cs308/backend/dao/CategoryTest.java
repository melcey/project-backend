package com.cs308.backend.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class CategoryTest {
    @Test
    void testCategoryCreation() {
        Category category = new Category("Electronics", "Devices and gadgets");

        assertEquals("Electronics", category.getName());
        assertEquals("Devices and gadgets", category.getDescription());
        assertNotNull(category.getProducts());
    }

    @Test
    void testSettersAndGetters() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Books");
        category.setDescription("All kinds of books");

        assertEquals(1L, category.getId());
        assertEquals("Books", category.getName());
        assertEquals("All kinds of books", category.getDescription());
    }

    @Test
    void testToString() {
        Category category = new Category("Electronics", "Devices and gadgets");

        String expected = "Category [id=null, name=Electronics, description=Devices and gadgets]";
        assertEquals(expected, category.toString());
    }
    
    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testHashCodeAndEquals() {
        Category category1 = new Category("Electronics", "Devices and gadgets");
        Category category2 = new Category("Electronics", "Devices and gadgets");
        Category category3 = new Category("Books", "All kinds of books");
        Category categoryNull = new Category();

        // Test equality for objects with the same fields
        category1.setId(1L);
        category2.setId(1L);
        assertEquals(category1, category2);
        assertEquals(category1.hashCode(), category2.hashCode());

        // Test inequality for objects with different fields
        category3.setId(2L);
        assertEquals(false, category1.equals(category3));
        assertEquals(false, category1.hashCode() == category3.hashCode());

        // Test equality for objects with null fields
        Category categoryNull2 = new Category();
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