package com.cs308.backend.repo;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cs308.backend.model.Category;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// The class to implement the interface CategoryRepositoryObj,
// which deals with queries where Category objects are going to be passed
// Follows the naming convention; therefore, Spring Data is going to wire this up into the CategoryRepositoryObj interface automatically
// Since the CategoryRepository also extends the CategoryRepositoryObj interface, these functions will be the ones running when a CategoryRepository instance invokes them
@Repository
@Transactional
public class CategoryRepositoryImpl implements CategoryRepositoryObj {
    // EntityManager will execute the generated native SQL query on the database
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Category insertNewCategory(Category category) {
        // Creates the query command in SQL to insert the new record to the table and return the inserted data
        String sqlQuery = "INSERT INTO categories (name, description) VALUES (:name, :description) RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Category object
        Category newCategory = (Category)entityManager.createNativeQuery(sqlQuery, Category.class)
            .setParameter("name", category.getName())
            .setParameter("description", category.getDescription())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return newCategory;
    }

    @Override
    public void deleteCategoryById(Category category) {
        // Creates the query command to delete the category with the given ID
        String sqlQuery = "DELETE FROM categories WHERE category_id = :id";
        
        // Creates the native query, injects the parameters, and executes the query
        entityManager.createNativeQuery(sqlQuery)
            .setParameter("id", category.getId())
            .executeUpdate();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
    }

    @Override
    public Category updateCategoryName(Category category, String newName) {
        // Creates the query command to update the name of the given category
        String sqlQuery = "UPDATE categories SET name = :new_name WHERE category_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Category object
        Category updatedCategory = (Category)entityManager.createNativeQuery(sqlQuery, Category.class)
            .setParameter("new_name", newName)
            .setParameter("id", category.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedCategory;
    }

    @Override
    public Category updateCategoryDescription(Category category, String newDescription) {
        // Creates the query command to update the description of the given category
        String sqlQuery = "UPDATE categories SET description = :new_description WHERE category_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Category object
        Category updatedCategory = (Category)entityManager.createNativeQuery(sqlQuery, Category.class)
            .setParameter("new_description", newDescription)
            .setParameter("id", category.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedCategory;
    }
}
