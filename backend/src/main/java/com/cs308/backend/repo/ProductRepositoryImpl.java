package com.cs308.backend.repo;

import java.math.BigDecimal;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cs308.backend.model.Category;
import com.cs308.backend.model.Product;
import com.cs308.backend.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// The class to implement the interface ProductRepositoryObj,
// which deals with queries where Product objects are going to be passed
// Follows the naming convention; therefore, Spring Data is going to wire this up into the ProductRepositoryObj interface automatically
// Since the ProductRepository also extends the ProductRepositoryObj interface, these functions will be the ones running when a ProductRepository instance invokes them
@Repository
@Transactional
public class ProductRepositoryImpl implements ProductRepositoryObj {
    // EntityManager will execute the generated native SQL query on the database
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product insertNewProduct(Product product) {
        // Creates the query command in SQL to insert the new record to the table and return the inserted data
        String sqlQuery = "INSERT INTO products (name, model, serial_number, description, quantity_in_stock, price, warranty_status, distributor_info, category_id, image_url, is_active, product_manager_id) VALUES (:name, :model, :serial_number, :description, :quantity_in_stock, :price, :warranty_status, :distributor_info, :category_id, :image_url, :is_active, :product_manager_id) RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product newProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("name", product.getName())
            .setParameter("model", product.getModel())
            .setParameter("serial_number", product.getSerialNumber())
            .setParameter("description", product.getDescription())
            .setParameter("quantity_in_stock", product.getQuantityInStock())
            .setParameter("price", product.getPrice())
            .setParameter("warranty_status", product.getWarrantyStatus())
            .setParameter("distributor_info", product.getDistributorInfo())
            .setParameter("category_id", product.getCategory().getId())
            .setParameter("image_url", product.getImageUrl())
            .setParameter("is_active", product.getIsActive())
            .setParameter("product_manager_id", product.getProductManager().getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return newProduct;
    }

    @Override
    public void deleteProductById(Product product) {
        // Creates the query command to delete the product with the given ID
        String sqlQuery = "DELETE FROM products WHERE product_id = :id";
        
        // Creates the native query, injects the parameters, and executes the query
        entityManager.createNativeQuery(sqlQuery)
            .setParameter("id", product.getId())
            .executeUpdate();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
    }

    @Override
    public void deleteProductByManager(User productManager) {
        // Creates the query command to delete the product with the given product manager ID
        String sqlQuery = "DELETE FROM products WHERE product_manager_id = :id";
        
        // Creates the native query, injects the parameters, and executes the query
        entityManager.createNativeQuery(sqlQuery)
            .setParameter("id", productManager.getId())
            .executeUpdate();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
    }

    @Override
    public Product updateProductName(Product product, String newName) {
        // Creates the query command to update the name of the given product
        String sqlQuery = "UPDATE products SET name = :new_name WHERE product_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product updatedProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("new_name", newName)
            .setParameter("id", product.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedProduct;
    }

    @Override
    public Product updateProductModel(Product product, String newModel) {
        // Creates the query command to update the model of the given product
        String sqlQuery = "UPDATE products SET model = :new_model WHERE product_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product updatedProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("new_model", newModel)
            .setParameter("id", product.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedProduct;
    }

    @Override
    public Product updateProductSerialNumber(Product product, String newSerialNumber) {
        // Creates the query command to update the serial number of the given product
        String sqlQuery = "UPDATE products SET serial_number = :new_serial_number WHERE product_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product updatedProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("new_serial_number", newSerialNumber)
            .setParameter("id", product.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedProduct;
    }

    @Override
    public Product updateProductDescription(Product product, String newDescription) {
        // Creates the query command to update the description of the given product
        String sqlQuery = "UPDATE products SET description = :new_description WHERE product_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product updatedProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("new_description", newDescription)
            .setParameter("id", product.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedProduct;
    }

    @Override
    public Product updateProductQuantityInStock(Product product, int newQuantityInStock) {
        // Creates the query command to update the stock quantity of the given product
        String sqlQuery = "UPDATE products SET quantity_in_stock = :new_quantity_in_stock WHERE product_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product updatedProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("new_quantity_in_stock", newQuantityInStock)
            .setParameter("id", product.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedProduct;
    }

    @Override
    public Product updateProductPrice(Product product, BigDecimal newPrice) {
        // Creates the query command to update the price of the given product
        String sqlQuery = "UPDATE products SET price = :new_price WHERE product_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product updatedProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("new_price", newPrice)
            .setParameter("id", product.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedProduct;
    }

    @Override
    public Product updateProductWarrantyStatus(Product product, String newWarrantyStatus) {
        // Creates the query command to update the warranty status of the given product
        String sqlQuery = "UPDATE products SET warranty_status = :new_warranty_status WHERE product_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product updatedProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("new_warranty_status", newWarrantyStatus)
            .setParameter("id", product.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedProduct;
    }

    @Override
    public Product updateProductDistributorInfo(Product product, String newDistributorInfo) {
        // Creates the query command to update the distributor info of the given product
        String sqlQuery = "UPDATE products SET distributor_info = :new_distributor_info WHERE product_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product updatedProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("new_distributor_info", newDistributorInfo)
            .setParameter("id", product.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedProduct;
    }

    @Override
    public Product updateProductIsActive(Product product, boolean newIsActive) {
        // Creates the query command to update the activity status of the given product
        String sqlQuery = "UPDATE products SET is_active = :new_is_active WHERE product_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product updatedProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("new_is_active", newIsActive)
            .setParameter("id", product.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedProduct;
    }

    @Override
    public Product updateProductCategory(Product product, Category newCategory) {
        // Creates the query command to update the category of the given product
        String sqlQuery = "UPDATE products SET category_id = :new_category_id WHERE product_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product updatedProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("new_category_id", newCategory.getId())
            .setParameter("id", product.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedProduct;
    }

    @Override
    public Product updateProductManager(Product product, User newProductManager) {
        // Creates the query command to update the product manager of the given product
        String sqlQuery = "UPDATE products SET product_manager_id = :new_product_manager_id WHERE product_id = :id RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a Product object
        Product updatedProduct = (Product)entityManager.createNativeQuery(sqlQuery, Product.class)
            .setParameter("new_product_manager_id", newProductManager.getId())
            .setParameter("id", product.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedProduct;
    }
}
