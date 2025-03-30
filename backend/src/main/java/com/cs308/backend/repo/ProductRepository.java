package com.cs308.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cs308.backend.model.Category;
import com.cs308.backend.model.Product;
import com.cs308.backend.model.User;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;



// Extending both JpaRepository<Product, Long> for findBy... queries
// and ProductRepositoryObj to be able to deal with Product objects passed as parameters
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryObj {
    // SELECT queries:
    Optional<Product> findById(Long id);

    List<Product> findByName(String name);
    List<Product> findByNameContains(String name);
    
    List<Product> findByModel(String model);
    List<Product> findByModelContains(String model);

    List<Product> findBySerialNumber(String serialNumber);

    List<Product> findByDescriptionContains(String description);

    List<Product> findByQuantityInStockEquals(int quantityInStock);
    List<Product> findByQuantityInStockLessThan(int quantityInStock);
    List<Product> findByQuantityInStockLessThanEqual(int quantityInStock);
    List<Product> findByQuantityInStockGreaterThan(int quantityInStock);
    List<Product> findByQuantityInStockGreaterThanEqual(int quantityInStock);

    List<Product> findByPriceEquals(BigDecimal price);
    List<Product> findByPriceLessThan(BigDecimal price);
    List<Product> findByPriceLessThanEqual(BigDecimal price);
    List<Product> findByPriceGreaterThan(BigDecimal price);
    List<Product> findByPriceGreaterThanEqual(BigDecimal price);

    List<Product> findByWarrantyStatus(String warrantyStatus);

    List<Product> findByDistributorInfo(String distributorInfo);
    List<Product> findByDistributorInfoContains(String distributorInfo);

    List<Product> findByIsActive(Boolean isActive);

    List<Product> findByCategory(Category category);

    @Query(value = "SELECT * FROM products WHERE category_id = :category_id", nativeQuery = true)
    List<Product> findByCategoryId(@Param("category_id") Long categoryId);

    List<Product> findByProductManager(User productManager);

    @Query(value = "SELECT * FROM products WHERE product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findByProductManagerId(@Param("product_manager_id") Long productManagerId);
}
