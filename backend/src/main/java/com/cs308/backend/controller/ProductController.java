package com.cs308.backend.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cs308.backend.dao.Comment;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Rating;
import com.cs308.backend.dto.CategoryResponse;
import com.cs308.backend.dto.ProductListResponse;
import com.cs308.backend.dto.ProductResponse;
import com.cs308.backend.service.CommentService;
import com.cs308.backend.service.ProductService;
import com.cs308.backend.service.RatingService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CommentService commentService;
    private final RatingService ratingService;

    public ProductController(ProductService productService, CommentService commentService, RatingService ratingService) {
        this.productService = productService;
        this.commentService = commentService;
        this.ratingService = ratingService;
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

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsForProduct(@PathVariable Long id) {
        Optional<Product> productOpt = productService.findProductById(id);

        if (!productOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }

        List<Comment> commentsForProduct = commentService.findApprovedCommentsForProduct(productOpt.get());

        return ResponseEntity.ok(commentsForProduct);
    }

    @GetMapping("/{id}/ratings")
    public ResponseEntity<?> getRatingsForProduct(@PathVariable Long id) {
        Optional<Product> productOpt = productService.findProductById(id);

        if (!productOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }

        List<Rating> ratingsForProduct = ratingService.findRatingsForProduct(productOpt.get());

        return ResponseEntity.ok(ratingsForProduct);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        List<Product> allRetrieved = productService.findAllProducts();
        List<ProductResponse> allProducts = new ArrayList<>();

        for (Product retrieved: allRetrieved) {
            if (retrieved.getIsActive()) {
                allProducts.add(new ProductResponse(retrieved.getId(), retrieved.getName(), retrieved.getModel(), retrieved.getSerialNumber(), retrieved.getDescription(), retrieved.getQuantityInStock(), retrieved.getPrice(), retrieved.getWarrantyStatus(), retrieved.getDistributorInfo(), retrieved.getIsActive(), retrieved.getImageUrl(), new CategoryResponse(retrieved.getCategory().getId(), retrieved.getCategory().getName(), retrieved.getCategory().getDescription())));
            }
        }

        return ResponseEntity.ok(new ProductListResponse(allProducts));
    }
    
    // A unified search endpoint for products
    // Example use for category search:
    //GET /products?categoryId=1&categoryId=2&categoryId=3
    @GetMapping
    public ResponseEntity<?> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String serialNumber,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String distributorInfo,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String warrantyStatus,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Integer maxQuantity,
            @RequestParam(required = false) List<Long> categoryId) {

            if ((name == null) && (model == null) && (serialNumber == null) &&
                (description == null) && (distributorInfo == null) && (isActive == null) &&
                (warrantyStatus == null) && (minPrice == null) && (maxPrice == null) &&
                (minQuantity == null) && (maxQuantity == null) && ((categoryId == null) || (categoryId.isEmpty()))) {
                return ResponseEntity.ok(new ProductListResponse());
            }

        List<Product> foundProducts = productService.searchProducts(name, model, serialNumber, description, distributorInfo, isActive,
                warrantyStatus, minPrice, maxPrice, minQuantity, maxQuantity, categoryId);
        List<ProductResponse> responseProductList = new ArrayList<>();

        for (Product foundProduct: foundProducts) {
            if (foundProduct.getIsActive()) {
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
        }

        return ResponseEntity.ok(new ProductListResponse(responseProductList));
    }
}