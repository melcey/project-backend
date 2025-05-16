package com.cs308.backend.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Category;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.CategoryRepository;
import com.cs308.backend.repo.ProductRepository;
import com.cs308.backend.dto.ProductRequest;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String name, String model, String serialNumber, String description,
            String distributorInfo, Boolean isActive, String warrantyStatus, BigDecimal minPrice, BigDecimal maxPrice,
            Integer minQuantity, Integer maxQuantity, List<Long> categoryIds) {

        List<Product> resultSet = productRepository.findAll();

        if (name != null) {
            resultSet.retainAll(findProductsByName(name));
        }
        if (model != null) {
            resultSet.retainAll(findProductsByModel(model));
        }
        if (serialNumber != null) {
            resultSet.retainAll(findProductsBySerialNumber(serialNumber));
        }
        if (description != null) {
            resultSet.retainAll(findProductsByDescriptionContains(description));
        }
        if (distributorInfo != null) {
            resultSet.retainAll(findProductsByDistributorInfo(distributorInfo));
        }
        if (isActive != null) {
            resultSet.retainAll(findProductsByIsActive(isActive));
        }
        if (warrantyStatus != null) {
            resultSet.retainAll(findProductsByWarrantyStatus(warrantyStatus));
        }
        if (minPrice != null) {
            resultSet.retainAll(findProductsByPriceGreaterThanEqual(minPrice));
        }
        if (maxPrice != null) {
            resultSet.retainAll(findProductsByPriceLessThanEqual(maxPrice));
        }
        if (minQuantity != null) {
            resultSet.retainAll(findProductsByQuantityInStockGreaterThanEqual(minQuantity));
        }
        if (maxQuantity != null) {
            resultSet.retainAll(findProductsByQuantityInStockLessThanEqual(maxQuantity));
        }
        if ((categoryIds != null) && (!categoryIds.isEmpty())) {
            Set<Product> categoryFilteredProducts = new HashSet<>();
            for (Long categoryId : categoryIds) {
                categoryFilteredProducts.addAll(findProductsByCategory(categoryRepository.findById(categoryId).get()));
            }

            resultSet.retainAll(categoryFilteredProducts);
        }

        return new ArrayList<>(resultSet);
    }

    public List<Product> searchManagedProducts(User productManager, String name, String model, String serialNumber,
            String description, String distributorInfo, Boolean isActive, String warrantyStatus, BigDecimal minPrice,
            BigDecimal maxPrice, Integer minQuantity, Integer maxQuantity, List<Long> categoryIds) {

        List<Product> resultSet = productRepository.findByProductManagerId(productManager.getId());

        if (name != null) {
            resultSet.retainAll(findManagedProductsByName(name, productManager));
        }
        if (model != null) {
            resultSet.retainAll(findManagedProductsByModel(model, productManager));
        }
        if (serialNumber != null) {
            resultSet.retainAll(findManagedProductsBySerialNumber(serialNumber, productManager));
        }
        if (description != null) {
            resultSet.retainAll(findManagedProductsByDescriptionContains(description, productManager));
        }
        if (distributorInfo != null) {
            resultSet.retainAll(findManagedProductsByDistributorInfo(distributorInfo, productManager));
        }
        if (isActive != null) {
            resultSet.retainAll(findManagedProductsByIsActive(isActive, productManager));
        }
        if (warrantyStatus != null) {
            resultSet.retainAll(findManagedProductsByWarrantyStatus(warrantyStatus, productManager));
        }
        if (minPrice != null) {
            resultSet.retainAll(findManagedProductsByPriceGreaterThanEqual(minPrice, productManager));
        }
        if (maxPrice != null) {
            resultSet.retainAll(findManagedProductsByPriceLessThanEqual(maxPrice, productManager));
        }
        if (minQuantity != null) {
            resultSet.retainAll(findManagedProductsByQuantityInStockGreaterThanEqual(minQuantity, productManager));
        }
        if (maxQuantity != null) {
            resultSet.retainAll(findManagedProductsByQuantityInStockLessThanEqual(maxQuantity, productManager));
        }
        if ((categoryIds != null) && (!(categoryIds.isEmpty()))) {
            Set<Product> categoryFilteredProducts = new HashSet<>();
            for (Long categoryId : categoryIds) {
                categoryFilteredProducts.addAll(
                        findManagedProductsByCategory(categoryRepository.findById(categoryId).get(), productManager));
            }

            resultSet.retainAll(categoryFilteredProducts);
        }

        return new ArrayList<>(resultSet);
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findProductsByName(String name) {
        Set<Product> foundProducts = new HashSet<>();
        List<Product> exactMatches = productRepository.findByName(name);

        if ((exactMatches != null) && (!(exactMatches.isEmpty()))) {
            foundProducts.addAll(exactMatches);
        }

        List<Product> containingMatches = productRepository.findByNameContains(name);
        if ((containingMatches != null) && (!(containingMatches.isEmpty()))) {
            foundProducts.addAll(containingMatches);
        }

        return new ArrayList<>(foundProducts);
    }

    public List<Product> findProductsByModel(String model) {
        Set<Product> foundProducts = new HashSet<>();
        List<Product> exactMatches = productRepository.findByModel(model);

        if ((exactMatches != null) && (!(exactMatches.isEmpty()))) {
            foundProducts.addAll(exactMatches);
        }

        List<Product> containingMatches = productRepository.findByModelContains(model);
        if ((containingMatches != null) && (!(containingMatches.isEmpty()))) {
            foundProducts.addAll(containingMatches);
        }

        return new ArrayList<>(foundProducts);
    }

    public List<Product> findProductsBySerialNumber(String serialNumber) {
        Set<Product> foundProducts = new HashSet<>();
        List<Product> exactMatches = productRepository.findBySerialNumber(serialNumber);

        if ((exactMatches != null) && (!(exactMatches.isEmpty()))) {
            foundProducts.addAll(exactMatches);
        }

        List<Product> containingMatches = productRepository.findBySerialNumberContains(serialNumber);
        if ((containingMatches != null) && (!(containingMatches.isEmpty()))) {
            foundProducts.addAll(containingMatches);
        }

        return new ArrayList<>(foundProducts);
    }

    public List<Product> findProductsByDescriptionContains(String description) {
        return productRepository.findByDescriptionContains(description);
    }

    public List<Product> findProductsByQuantityInStockEquals(int quantity) {
        return productRepository.findByQuantityInStockEquals(quantity);
    }

    public List<Product> findProductsByQuantityInStockLessThan(int quantity) {
        return productRepository.findByQuantityInStockLessThan(quantity);
    }

    public List<Product> findProductsByQuantityInStockLessThanEqual(int quantity) {
        return productRepository.findByQuantityInStockLessThanEqual(quantity);
    }

    public List<Product> findProductsByQuantityInStockGreaterThan(int quantity) {
        return productRepository.findByQuantityInStockGreaterThan(quantity);
    }

    public List<Product> findProductsByQuantityInStockGreaterThanEqual(int quantity) {
        return productRepository.findByQuantityInStockGreaterThanEqual(quantity);
    }

    public List<Product> findProductsByPriceEquals(BigDecimal price) {
        return productRepository.findByPriceEquals(price);
    }

    public List<Product> findProductsByPriceLessThan(BigDecimal price) {
        return productRepository.findByPriceLessThan(price);
    }

    public List<Product> findProductsByPriceLessThanEqual(BigDecimal price) {
        return productRepository.findByPriceLessThanEqual(price);
    }

    public List<Product> findProductsByPriceGreaterThan(BigDecimal price) {
        return productRepository.findByPriceGreaterThan(price);
    }

    public List<Product> findProductsByPriceGreaterThanEqual(BigDecimal price) {
        return productRepository.findByPriceGreaterThanEqual(price);
    }

    public List<Product> findProductsByWarrantyStatus(String warrantyStatus) {
        return productRepository.findByWarrantyStatus(warrantyStatus);
    }

    public List<Product> findProductsByDistributorInfo(String distributorInfo) {
        Set<Product> foundProducts = new HashSet<>();
        List<Product> exactMatches = productRepository.findByDistributorInfo(distributorInfo);

        if ((exactMatches != null) && (!(exactMatches.isEmpty()))) {
            foundProducts.addAll(exactMatches);
        }

        List<Product> containingMatches = productRepository.findByDistributorInfoContains(distributorInfo);
        if ((containingMatches != null) && (!(containingMatches.isEmpty()))) {
            foundProducts.addAll(containingMatches);
        }

        return new ArrayList<>(foundProducts);
    }

    public List<Product> findProductsByIsActive(boolean isActive) {
        return productRepository.findByIsActive(isActive);
    }

    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findByCategoryId(category.getId());
    }

    public List<Product> findProductsByProductManager(User productManager) {
        return productRepository.findByProductManagerId(productManager.getId());
    }

    public Optional<Product> findManagedProductById(Long id, User productManager) {
        return productRepository.findManagedProductById(id, productManager.getId());
    }

    public List<Product> findManagedProductsByName(String name, User productManager) {
        Set<Product> foundProducts = new HashSet<>();
        List<Product> exactMatches = productRepository.findManagedByName(name, productManager.getId());

        if ((exactMatches != null) && (!(exactMatches.isEmpty()))) {
            foundProducts.addAll(exactMatches);
        }

        List<Product> containingMatches = productRepository.findManagedByNameContains(name, productManager.getId());
        if ((containingMatches != null) && (!(containingMatches.isEmpty()))) {
            foundProducts.addAll(containingMatches);
        }

        return new ArrayList<>(foundProducts);
    }

    public List<Product> findManagedProductsByModel(String model, User productManager) {
        Set<Product> foundProducts = new HashSet<>();
        List<Product> exactMatches = productRepository.findManagedByModel(model, productManager.getId());

        if ((exactMatches != null) && (!(exactMatches.isEmpty()))) {
            foundProducts.addAll(exactMatches);
        }

        List<Product> containingMatches = productRepository.findManagedByModelContains(model, productManager.getId());
        if ((containingMatches != null) && (!(containingMatches.isEmpty()))) {
            foundProducts.addAll(containingMatches);
        }

        return new ArrayList<>(foundProducts);
    }

    public List<Product> findManagedProductsBySerialNumber(String serialNumber, User productManager) {
        Set<Product> foundProducts = new HashSet<>();
        List<Product> exactMatches = productRepository.findManagedBySerialNumber(serialNumber, productManager.getId());

        if ((exactMatches != null) && (!(exactMatches.isEmpty()))) {
            foundProducts.addAll(exactMatches);
        }

        List<Product> containingMatches = productRepository.findManagedBySerialNumberContains(serialNumber,
                productManager.getId());
        if ((containingMatches != null) && (!(containingMatches.isEmpty()))) {
            foundProducts.addAll(containingMatches);
        }

        return new ArrayList<>(foundProducts);
    }

    public List<Product> findManagedProductsByDescriptionContains(String description, User productManager) {
        return productRepository.findManagedByDescriptionContains(description, productManager.getId());
    }

    public List<Product> findManagedProductsByQuantityInStockEquals(int quantity, User productManager) {
        return productRepository.findManagedByQuantityInStockEquals(quantity, productManager.getId());
    }

    public List<Product> findManagedProductsByQuantityInStockLessThan(int quantity, User productManager) {
        return productRepository.findManagedByQuantityInStockLessThan(quantity, productManager.getId());
    }

    public List<Product> findManagedProductsByQuantityInStockLessThanEqual(int quantity, User productManager) {
        return productRepository.findManagedByQuantityInStockLessThanEqual(quantity, productManager.getId());
    }

    public List<Product> findManagedProductsByQuantityInStockGreaterThan(int quantity, User productManager) {
        return productRepository.findManagedByQuantityInStockGreaterThan(quantity, productManager.getId());
    }

    public List<Product> findManagedProductsByQuantityInStockGreaterThanEqual(int quantity, User productManager) {
        return productRepository.findManagedByQuantityInStockGreaterThanEqual(quantity, productManager.getId());
    }

    public List<Product> findManagedProductsByPriceEquals(BigDecimal price, User productManager) {
        return productRepository.findManagedByPriceEquals(price, productManager.getId());
    }

    public List<Product> findManagedProductsByPriceLessThan(BigDecimal price, User productManager) {
        return productRepository.findManagedByPriceLessThan(price, productManager.getId());
    }

    public List<Product> findManagedProductsByPriceLessThanEqual(BigDecimal price, User productManager) {
        return productRepository.findManagedByPriceLessThanEqual(price, productManager.getId());
    }

    public List<Product> findManagedProductsByPriceGreaterThan(BigDecimal price, User productManager) {
        return productRepository.findManagedByPriceGreaterThan(price, productManager.getId());
    }

    public List<Product> findManagedProductsByPriceGreaterThanEqual(BigDecimal price, User productManager) {
        return productRepository.findManagedByPriceGreaterThanEqual(price, productManager.getId());
    }

    public List<Product> findManagedProductsByWarrantyStatus(String warrantyStatus, User productManager) {
        return productRepository.findManagedByWarrantyStatus(warrantyStatus, productManager.getId());
    }

    public List<Product> findManagedProductsByDistributorInfo(String distributorInfo, User productManager) {
        Set<Product> foundProducts = new HashSet<>();
        List<Product> exactMatches = productRepository.findManagedByDistributorInfo(distributorInfo,
                productManager.getId());

        if ((exactMatches != null) && (!(exactMatches.isEmpty()))) {
            foundProducts.addAll(exactMatches);
        }

        List<Product> containingMatches = productRepository.findManagedByDistributorInfoContains(distributorInfo,
                productManager.getId());
        if ((containingMatches != null) && (!(containingMatches.isEmpty()))) {
            foundProducts.addAll(containingMatches);
        }

        return new ArrayList<>(foundProducts);
    }

    public List<Product> findManagedProductsByIsActive(boolean isActive, User productManager) {
        return productRepository.findManagedByIsActive(isActive, productManager.getId());
    }

    public List<Product> findManagedProductsByCategory(Category category, User productManager) {
        return productRepository.findManagedByCategoryId(category.getId(), productManager.getId());
    }

    public Optional<Product> createProduct(Product product) {
        return productRepository.insertNewProduct(product);
    }

    public void deleteProduct(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);

        productOpt.ifPresent(productRepository::deleteProduct);
    }

    public void deleteProductsByManager(User productManager) {
        productRepository.deleteProductsByManager(productManager);
    }

    public Optional<Product> updateProductName(Long productId, String newName) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductName(product, newName))
                .orElse(Optional.empty());
    }

    public Optional<Product> updateProductModel(Long productId, String newModel) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductModel(product, newModel))
                .orElse(Optional.empty());
    }

    public Optional<Product> updateProductSerialNumber(Long productId, String newSerialNumber) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductSerialNumber(product, newSerialNumber))
                .orElse(Optional.empty());
    }

    public Optional<Product> updateProductDescription(Long productId, String newDescription) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductDescription(product, newDescription))
                .orElse(Optional.empty());
    }

    public Optional<Product> updateProductQuantityInStock(Long productId, int newQuantityInStock) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductQuantityInStock(product, newQuantityInStock))
                .orElse(Optional.empty());
    }

    public Optional<Product> updateProductPrice(Long productId, BigDecimal newPrice) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductPrice(product, newPrice))
                .orElse(Optional.empty());
    }

    public Optional<Product> updateProductWarrantyStatus(Long productId, String newWarrantyStatus) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductWarrantyStatus(product, newWarrantyStatus))
                .orElse(Optional.empty());
    }

    public Optional<Product> updateProductDistributorInfo(Long productId, String newDistributorInfo) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductDistributorInfo(product, newDistributorInfo))
                .orElse(Optional.empty());
    }

    public Optional<Product> updateProductIsActive(Long productId, boolean newIsActive) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductIsActive(product, newIsActive))
                .orElse(Optional.empty());
    }

    public Optional<Product> updateProductImageUrl(Long productId, String newImageUrl) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductImageUrl(product, newImageUrl))
                .orElse(Optional.empty());
    }

    public Optional<Product> updateProductCategory(Long productId, Long newCategoryId) {
        Optional<Category> newCategory = categoryRepository.findById(newCategoryId);

        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductCategory(product, newCategory.get()))
                .orElse(Optional.empty());
    }

    public Optional<Product> updateProductManager(Long productId, User newProductManager) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductManager(product, newProductManager))
                .orElse(Optional.empty());
    }

    public ResponseEntity<?> addProduct(ProductRequest request) {
        try {
            Product product = new Product(
                    request.getName(),
                    request.getModel(),
                    request.getSerialNumber(),
                    request.getDescription(),
                    request.getQuantityInStock(),
                    request.getPrice(),
                    request.getWarrantyStatus(),
                    request.getDistributorInfo(),
                    true,
                    request.getImageUrl());

            if (request.getCategoryId() != null) {
                Category category = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found"));
                product.setCategory(category);
            }

            Product savedProduct = productRepository.save(product);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding product: " + e.getMessage());
        }
    }

    public ResponseEntity<?> removeProduct(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            product.setIsActive(false);
            productRepository.save(product);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error removing product: " + e.getMessage());
        }
    }

    public ResponseEntity<?> updateStock(Long productId, int newStock) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (newStock < 0) {
                return ResponseEntity.badRequest().body("Stock cannot be negative");
            }

            product.setQuantityInStock(newStock);
            Product updatedProduct = productRepository.save(product);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating stock: " + e.getMessage());
        }
    }
}