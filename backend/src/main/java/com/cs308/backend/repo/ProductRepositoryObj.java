package com.cs308.backend.repo;

import java.math.BigDecimal;

import com.cs308.backend.model.Category;
import com.cs308.backend.model.Product;
import com.cs308.backend.model.User;

// The specific interface to deal with queries
// for which the entire Product objects will need to be passed in Java
public interface ProductRepositoryObj {
    // The method to insert a new product into the `products` table
    Product insertNewProduct(Product product);

    // The method to delete a product from the `products` table given its ID
    void deleteProductById(Product product);

    // The method to delete a product from the `products` table given its product manager
    void deleteProductByManager(User productManager);

    // The method to update a product's name
    Product updateProductName(Product product, String newName);

    // The method to update a product's model
    Product updateProductModel(Product product, String newModel);

    // The method to update a product's serial number
    Product updateProductSerialNumber(Product product, String newSerialNumber);

    // The method to update a product's description
    Product updateProductDescription(Product product, String newDescription);

    // The method to update a product's stock quantity
    Product updateProductQuantityInStock(Product product, int newQuantityInStock);

    // The method to update a product's price
    Product updateProductPrice(Product product, BigDecimal newPrice);

    // The method to update a product's warranty status
    Product updateProductWarrantyStatus(Product product, String newWarrantyStatus);

    // The method to update a product's distributor info
    Product updateProductDistributorInfo(Product product, String newDistributorInfo);

    // The method to update a product's activity status
    Product updateProductIsActive(Product product, boolean newIsActive);

    // The method to update a product's category
    Product updateProductCategory(Product product, Category newCategory);

    // The method to update a product's product manager
    Product updateProductManager(Product product, User newProductManager);
}
