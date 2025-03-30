package com.cs308.backend.service;

import com.cs308.backend.model.Category;
import com.cs308.backend.model.Product;
import com.cs308.backend.model.User;
import com.cs308.backend.repo.CategoryRepository;
import com.cs308.backend.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    
    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findProductsByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContains(name);
    }

    public List<Product> findProductsByModel(String model) {
        return productRepository.findByModel(model);
    }

    public List<Product> searchProductsByModel(String model) {
        return productRepository.findByModelContains(model);
    }

    public List<Product> findProductsBySerialNumber(String serialNumber) {
        return productRepository.findBySerialNumber(serialNumber);
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
        return productRepository.findByDistributorInfo(distributorInfo);
    }
    
    public List<Product> findProductsByDistributorInfoContains(String distributorInfo) {
        return productRepository.findByDistributorInfoContains(distributorInfo);
    }
    
    public List<Product> findProductsByIsActive(Boolean isActive) {
        return productRepository.findByIsActive(isActive);
    }
    
    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }
    
    public List<Product> findProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    
    public List<Product> findProductsByProductManager(User productManager) {
        return productRepository.findByProductManager(productManager);
    }
    
    public List<Product> findProductsByProductManagerId(Long productManagerId) {
        return productRepository.findByProductManagerId(productManagerId);
    }

    
    
    public Product createProduct(Product product) {
        return productRepository.insertNewProduct(product);
    }
    
    
    
    public void deleteProduct(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        productOpt.ifPresent(productRepository::deleteProductById);
    }
    
    public void deleteProductsByManager(User productManager) {
        productRepository.deleteProductByManager(productManager);
    }
    
    
    
    public Product updateProductName(Long productId, String newName) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductName(product, newName))
                .orElse(null);
    }
    
    public Product updateProductModel(Long productId, String newModel) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductModel(product, newModel))
                .orElse(null);
    }
    
    public Product updateProductSerialNumber(Long productId, String newSerialNumber) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductSerialNumber(product, newSerialNumber))
                .orElse(null);
    }
    
    public Product updateProductDescription(Long productId, String newDescription) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductDescription(product, newDescription))
                .orElse(null);
    }
    
    public Product updateProductQuantityInStock(Long productId, int newQuantityInStock) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductQuantityInStock(product, newQuantityInStock))
                .orElse(null);
    }
    
    public Product updateProductPrice(Long productId, BigDecimal newPrice) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductPrice(product, newPrice))
                .orElse(null);
    }
    
    public Product updateProductWarrantyStatus(Long productId, String newWarrantyStatus) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductWarrantyStatus(product, newWarrantyStatus))
                .orElse(null);
    }
    
    public Product updateProductDistributorInfo(Long productId, String newDistributorInfo) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductDistributorInfo(product, newDistributorInfo))
                .orElse(null);
    }
    
    public Product updateProductIsActive(Long productId, boolean newIsActive) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductIsActive(product, newIsActive))
                .orElse(null);
    }
    
    public Product updateProductCategory(Long productId, String newCategoryName) {
        Category newCategory = categoryRepository.findByName(newCategoryName).get(0);

        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductCategory(product, newCategory))
                .orElse(null);
    }
    
    public Product updateProductManager(Long productId, User newProductManager) {
        return productRepository.findById(productId)
                .map(product -> productRepository.updateProductManager(product, newProductManager))
                .orElse(null);
    }
}