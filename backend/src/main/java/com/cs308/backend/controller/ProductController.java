package com.cs308.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dto.CategoryResponse;
import com.cs308.backend.dto.ProductListResponse;
import com.cs308.backend.dto.ProductResponse;
import com.cs308.backend.dto.SearchProductRequest;
import com.cs308.backend.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Retrieve a product by its ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> productOpt = productService.findProductById(id);

        if (!productOpt.isPresent()) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }

        return ResponseEntity.ok(new ProductResponse(productOpt.get().getId(), productOpt.get().getName(), productOpt.get().getModel(), productOpt.get().getSerialNumber(), productOpt.get().getDescription(), productOpt.get().getQuantityInStock(), productOpt.get().getPrice(), productOpt.get().getWarrantyStatus(), productOpt.get().getDistributorInfo(), productOpt.get().getIsActive(), productOpt.get().getImageUrl(), new CategoryResponse(productOpt.get().getCategory().getId(), productOpt.get().getCategory().getName(), productOpt.get().getCategory().getDescription())));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        List<Product> allRetrieved = productService.findAllProducts();
        List<ProductResponse> allProducts = new ArrayList<>();

        for (Product retrieved: allRetrieved) {
            allProducts.add(new ProductResponse(retrieved.getId(), retrieved.getName(), retrieved.getModel(), retrieved.getSerialNumber(), retrieved.getDescription(), retrieved.getQuantityInStock(), retrieved.getPrice(), retrieved.getWarrantyStatus(), retrieved.getDistributorInfo(), retrieved.getIsActive(), retrieved.getImageUrl(), new CategoryResponse(retrieved.getCategory().getId(), retrieved.getCategory().getName(), retrieved.getCategory().getDescription())));
        }

        return ResponseEntity.ok(new ProductListResponse(allProducts));
    }
    
    // A unified search endpoint for products
    @GetMapping
    public ResponseEntity<?> searchProducts(@RequestBody SearchProductRequest productToSearch) {

        if (productToSearch == null) {
            return ResponseEntity.ok(new ProductListResponse());
        }

        if ((productToSearch.getName() == null) && (productToSearch.getModel() == null) && (productToSearch.getSerialNumber() == null) &&
            (productToSearch.getDescription() == null) && (productToSearch.getDistributorInfo() == null) && (productToSearch.getIsActive() == null) &&
            (productToSearch.getWarrantyStatus() == null) && (productToSearch.getMinPrice() == null) && (productToSearch.getMaxPrice() == null) &&
            (productToSearch.getMinQuantity() == null) && (productToSearch.getMaxQuantity() == null) && ((productToSearch.getCategoryIds() == null) || (productToSearch.getCategoryIds().isEmpty()))) {
            return ResponseEntity.ok(new ProductListResponse());
        }

        List<Product> foundProducts = productService.searchProducts(productToSearch.getName(), productToSearch.getModel(), productToSearch.getSerialNumber(), productToSearch.getDescription(), productToSearch.getDistributorInfo(), productToSearch.getIsActive(),
            productToSearch.getWarrantyStatus(), productToSearch.getMinPrice(), productToSearch.getMaxPrice(), productToSearch.getMinQuantity(), productToSearch.getMaxQuantity(), productToSearch.getCategoryIds());
        List<ProductResponse> responseProductList = new ArrayList<>();

        for (Product foundProduct: foundProducts) {
            responseProductList.add(
                new ProductResponse(
                    foundProduct.getId(),
                    foundProduct.getName(),
                    foundProduct.getModel(),
                    foundProduct.getSerialNumber(),
                    foundProduct.getDescription(),
                    foundProduct.getQuantityInStock(),
                    foundProduct.getPrice(),
                    foundProduct.getWarrantyStatus(),
                    foundProduct.getDistributorInfo(),
                    foundProduct.getIsActive(),
                    foundProduct.getImageUrl(),
                    new CategoryResponse(
                        foundProduct.getCategory().getId(),
                        foundProduct.getCategory().getName(),
                        foundProduct.getCategory().getDescription()
                    )
                )
            );
        }

        return ResponseEntity.ok(new ProductListResponse(responseProductList));
    }
}