package com.cs308.backend.repo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cs308.backend.dao.Product;

// Extending both JpaRepository<Product, Long> for findBy... queries
// and ProductRepositoryObj to be able to deal with Product objects passed as parameters
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryObj {
    // SELECT queries:
    List<Product> findAll();

    Optional<Product> findById(Long id);

    List<Product> findByName(String name);
    List<Product> findByNameContains(String name);
    
    List<Product> findByModel(String model);
    List<Product> findByModelContains(String model);

    List<Product> findBySerialNumber(String serialNumber);
    List<Product> findBySerialNumberContains(String serialNumber);

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

    List<Product> findByIsActive(boolean isActive);

    List<Product> findByIsPriced(boolean isPriced);

    @Query(value = "SELECT * FROM products WHERE category_id = :category_id", nativeQuery = true)
    List<Product> findByCategoryId(@Param("category_id") Long categoryId);

    @Query(value = "SELECT * FROM products WHERE product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findByProductManagerId(@Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE product_id = :id AND product_manager_id = :product_manager_id", nativeQuery = true)
    Optional<Product> findManagedProductById(@Param("id") Long id, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE name = :name AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByName(@Param("name") String name, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE name LIKE CONCAT('%', :name, '%') AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByNameContains(@Param("name") String name, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE model = :model AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByModel(@Param("model") String model, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE model LIKE CONCAT('%', :model, '%') AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByModelContains(@Param("model") String model, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE serial_number = :serial_number AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedBySerialNumber(@Param("serial_number") String serialNumber, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE model LIKE CONCAT('%', :serial_number, '%') AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedBySerialNumberContains(@Param("serial_number") String serialNumber, @Param("product_manager_id") Long productManagerId);
    
    @Query(value = "SELECT * FROM products WHERE description LIKE CONCAT('%', :description, '%') AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByDescriptionContains(@Param("description") String description, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE quantity_in_stock = :quantity_in_stock AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByQuantityInStockEquals(@Param("quantity_in_stock") int quantityInStock, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE quantity_in_stock < :quantity_in_stock AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByQuantityInStockLessThan(@Param("quantity_in_stock") int quantityInStock, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE quantity_in_stock <= :quantity_in_stock AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByQuantityInStockLessThanEqual(@Param("quantity_in_stock") int quantityInStock, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE quantity_in_stock > :quantity_in_stock AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByQuantityInStockGreaterThan(@Param("quantity_in_stock") int quantityInStock, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE quantity_in_stock >= :quantity_in_stock AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByQuantityInStockGreaterThanEqual(@Param("quantity_in_stock") int quantityInStock, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE price = :price AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByPriceEquals(@Param("price") BigDecimal price, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE price < :price AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByPriceLessThan(@Param("price") BigDecimal price, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE price <= :price AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByPriceLessThanEqual(@Param("price") BigDecimal price, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE price > :price AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByPriceGreaterThan(@Param("price") BigDecimal price, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE price >= :price AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByPriceGreaterThanEqual(@Param("price") BigDecimal price, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE warranty_status = :warranty_status AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByWarrantyStatus(@Param("warranty_status") String warrantyStatus, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE distributor_info = :distributor_info AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByDistributorInfo(@Param("distributor_info") String distributorInfo, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE distributor_info LIKE CONCAT('%', :distributor_info, '%') AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByDistributorInfoContains(@Param("distributor_info") String distributorInfo, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE is_active = :is_active AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByIsActive(@Param("is_active") boolean isActive, @Param("product_manager_id") Long productManagerId);

    @Query(value = "SELECT * FROM products WHERE category_id = :category_id AND product_manager_id = :product_manager_id", nativeQuery = true)
    List<Product> findManagedByCategoryId(@Param("category_id") Long categoryId, @Param("product_manager_id") Long productManagerId);
}
