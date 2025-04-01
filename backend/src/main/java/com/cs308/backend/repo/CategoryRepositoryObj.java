package com.cs308.backend.repo;

import com.cs308.backend.dao.Category;

// The specific interface to deal with queries
// for which the entire Category objects will need to be passed in Java
public interface CategoryRepositoryObj {
    // The method to insert a new category into the `categories` table
    Category insertNewCategory(Category category);

    // The method to delete a category from the `categories` table given its ID
    void deleteCategory(Category category);

    // The method to update a category's name
    Category updateCategoryName(Category category, String newName);

    // The method to update a category's description
    Category updateCategoryDescription(Category category, String newDescription);
}
