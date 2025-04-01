package com.cs308.backend.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cs308.backend.dao.Category;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.CategoryResponse;
import com.cs308.backend.dto.CreateProductRequest;
import com.cs308.backend.dto.ProductListResponse;
import com.cs308.backend.dto.ProductManagerRequest;
import com.cs308.backend.dto.ProductResponse;
import com.cs308.backend.dto.UpdateProductRequest;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.ProductService;
import com.cs308.backend.service.UserService;

@RestController
@RequestMapping("/prodman")
public class ProductManagerController {
    private final ProductService productService;
    private final UserService userService;

    public ProductManagerController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    // Get all the products you manage as a product manager
    @GetMapping("/managed")
    public ResponseEntity<?> getManagedProducts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        List<Product> managedProducts = productService.findProductsByProductManager(user);
        List<ProductResponse> managedProductResponse = new ArrayList<>();

        for (Product managedProduct: managedProducts) {
            managedProductResponse.add(new ProductResponse(managedProduct.getId(), managedProduct.getName(), managedProduct.getModel(), managedProduct.getSerialNumber(), managedProduct.getDescription(), managedProduct.getQuantityInStock(), managedProduct.getPrice(), managedProduct.getWarrantyStatus(), managedProduct.getDistributorInfo(), managedProduct.getIsActive(), managedProduct.getImageUrl(), new CategoryResponse(managedProduct.getCategory().getId(), managedProduct.getCategory().getName(), managedProduct.getCategory().getDescription())));
        }

        return ResponseEntity.ok(new ProductListResponse(managedProductResponse));
    }

    // Endpoint for individual products you manage as a product manager
    @GetMapping("/{id}")
    public ResponseEntity<?> getManagedProductById(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findManagedProductById(id, user);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such product");
        }

        return ResponseEntity.ok(new ProductResponse(foundProduct.get().getId(), foundProduct.get().getName(), foundProduct.get().getModel(), foundProduct.get().getSerialNumber(), foundProduct.get().getDescription(), foundProduct.get().getQuantityInStock(), foundProduct.get().getPrice(), foundProduct.get().getWarrantyStatus(), foundProduct.get().getDistributorInfo(), foundProduct.get().getIsActive(), foundProduct.get().getImageUrl(), new CategoryResponse(foundProduct.get().getCategory().getId(), foundProduct.get().getCategory().getName(), foundProduct.get().getCategory().getDescription())));
    }


    // Search endpoint for products you manage as a product manager
    @GetMapping
    public ResponseEntity<?> searchManagedProducts(
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
            @RequestParam(required = false) Integer maxQuantity) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        if ((name == null) && (model == null) && (serialNumber == null) &&
            (description == null) && (distributorInfo == null) && (isActive == null) &&
            (warrantyStatus == null) && (minPrice == null) && (maxPrice == null) &&
            (minQuantity == null) && (maxQuantity == null)) {
            return ResponseEntity.ok(new ProductListResponse());
        }

        List<Product> foundProducts = productService.searchManagedProducts(user, name, model, serialNumber, description, distributorInfo, isActive,
                warrantyStatus, minPrice, maxPrice, minQuantity, maxQuantity);
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

    // Create a new product
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Product product = new Product(createProductRequest.getName(), createProductRequest.getModel(), createProductRequest.getSerialNumber(), createProductRequest.getDescription(), createProductRequest.getQuantityInStock(), createProductRequest.getPrice(), createProductRequest.getWarrantyStatus(), createProductRequest.getDistributorInfo(), createProductRequest.getIsActive(), createProductRequest.getImageUrl());
        Optional<Category> category = productService.findCategoryById(createProductRequest.getCategoryId());

        if (!(category.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such category");
        }

        product.setCategory(category.get());
        product.setProductManager(user);
        Product createdProduct = productService.createProduct(product);

        return ResponseEntity.ok(new ProductResponse(createdProduct.getId(), createdProduct.getName(), createdProduct.getModel(), createdProduct.getSerialNumber(), createdProduct.getDescription(), createdProduct.getQuantityInStock(), createdProduct.getPrice(), createdProduct.getWarrantyStatus(), createdProduct.getDistributorInfo(), createdProduct.getIsActive(), createdProduct.getImageUrl(), new CategoryResponse(createdProduct.getCategory().getId(), createdProduct.getCategory().getName(), createdProduct.getCategory().getDescription())));
    }

    // Delete a product by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }
        
        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }

        productService.deleteProduct(id);

        return ResponseEntity.ok("OK");
    }

    // Unified update endpoint for the products
    // Note: Product manager changes are going to be handled at a separate endpoint
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest updateProductRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }
        
        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }

        Product product = foundProduct.get();

        if (updateProductRequest == null) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "No updates to the product");
        }
        else if ((updateProductRequest.getName() == null) && (updateProductRequest.getModel() == null) && (updateProductRequest.getSerialNumber() == null) && (updateProductRequest.getDescription() == null) && (updateProductRequest.getQuantityInStock() == null) && ((updateProductRequest.getPrice() == null)) && (updateProductRequest.getWarrantyStatus() == null) && (updateProductRequest.getDistributorInfo() == null) && (updateProductRequest.getIsActive() == null) && (updateProductRequest.getCategory() == null)) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "No updates to the product");
        }

        if (updateProductRequest.getName() != null) {
            product = productService.updateProductName(id, updateProductRequest.getName());
        }

        if (updateProductRequest.getModel() != null) {
            product = productService.updateProductModel(id, updateProductRequest.getModel());
        }

        if (updateProductRequest.getSerialNumber() != null) {
            product = productService.updateProductSerialNumber(id, updateProductRequest.getSerialNumber());
        }

        if (updateProductRequest.getDescription() != null) {
            product = productService.updateProductDescription(id, updateProductRequest.getDescription());
        }

        if (updateProductRequest.getQuantityInStock() != null) {
            product = productService.updateProductQuantityInStock(id, updateProductRequest.getQuantityInStock());
        }

        if (updateProductRequest.getPrice() != null) {
            product = productService.updateProductPrice(id, updateProductRequest.getPrice());
        }

        if (updateProductRequest.getWarrantyStatus() != null) {
            product = productService.updateProductWarrantyStatus(id, updateProductRequest.getWarrantyStatus());
        }

        if (updateProductRequest.getDistributorInfo() != null) {
            product = productService.updateProductDistributorInfo(id, updateProductRequest.getDistributorInfo());
        }

        if (updateProductRequest.getIsActive() != null) {
            product = productService.updateProductIsActive(id, updateProductRequest.getIsActive());
        }

        if (updateProductRequest.getImageUrl() != null) {
            product = productService.updateProductImageUrl(id, updateProductRequest.getImageUrl());
        }

        if (updateProductRequest.getCategory() != null) {
            product = productService.updateProductCategory(id, updateProductRequest.getCategory());
        }
        
        // Returns the old price?
        return ResponseEntity.ok(new ProductResponse(product.getId(), product.getName(), product.getModel(), product.getSerialNumber(), product.getDescription(), product.getQuantityInStock(), product.getPrice(), product.getWarrantyStatus(), product.getDistributorInfo(), product.getIsActive(), product.getImageUrl(), new CategoryResponse(product.getCategory().getId(), product.getCategory().getName(), product.getCategory().getDescription())));
    }

    // Update product name
    @PutMapping("/{id}/name")
    public ResponseEntity<?> updateProductName(@PathVariable Long id, @RequestBody String newName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }

        Product updatedProduct = productService.updateProductName(id, newName);

        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }

    // Update product model
    @PutMapping("/{id}/model")
    public ResponseEntity<?> updateProductModel(@PathVariable Long id, @RequestBody String newModel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }

        Product updatedProduct = productService.updateProductModel(id, newModel);

        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }

    // Update product serial number
    @PutMapping("/{id}/serial")
    public ResponseEntity<?> updateProductSerialNumber(@PathVariable Long id, @RequestBody String newSerialNumber) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }

        Product updatedProduct = productService.updateProductSerialNumber(id, newSerialNumber);

        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }

    // Update product description
    @PutMapping("/{id}/description")
    public ResponseEntity<?> updateProductDescription(@PathVariable Long id, @RequestBody String newDescription) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }

        Product updatedProduct = productService.updateProductDescription(id, newDescription);
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }

    // Update product stock quantity
    @PutMapping("/{id}/stock")
    public ResponseEntity<?> updateProductQuantity(@PathVariable Long id, @RequestBody int newQuantity) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }
        
        Product updatedProduct = productService.updateProductQuantityInStock(id, newQuantity);
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }

    // Update product price
    @PutMapping("/{id}/price")
    public ResponseEntity<?> updateProductPrice(@PathVariable Long id, @RequestBody BigDecimal newPrice) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }

        Product updatedProduct = productService.updateProductPrice(id, newPrice);
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }

    // Update warranty status
    @PutMapping("/{id}/warranty")
    public ResponseEntity<?> updateProductWarrantyStatus(@PathVariable Long id, @RequestBody String newWarrantyStatus) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }

        Product updatedProduct = productService.updateProductWarrantyStatus(id, newWarrantyStatus);
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }
    
    // Update distributor info
    @PutMapping("/{id}/distributor")
    public ResponseEntity<?> updateProductDistributorInfo(@PathVariable Long id, @RequestBody String newDistributorInfo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }

        Product updatedProduct = productService.updateProductDistributorInfo(id, newDistributorInfo);
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }
    
    // Activate the product
    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateProduct(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }
        else if (foundProduct.get().getIsActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is already activated");
        }

        Product updatedProduct = productService.updateProductIsActive(id, true);
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }

    // Deactivate the product
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateProduct(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }
        else if (!(foundProduct.get().getIsActive())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is already deactivated");
        }

        Product updatedProduct = productService.updateProductIsActive(id, false);
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }
    
    // Update category
    @PutMapping("/{id}/category")
    public ResponseEntity<?> updateProductCategory(@PathVariable Long id, @RequestBody String newCategoryName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }

        Product updatedProduct = productService.updateProductCategory(id, newCategoryName);
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }
    
    // Update product manager
    @PutMapping("/{id}/product_manager")
    public ResponseEntity<?> updateProductManager(@PathVariable Long id, @RequestBody ProductManagerRequest newProductManagerRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<User> foundProductManager = userService.findByIdAndRole(id, Role.product_manager);
        if (!(foundProductManager.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such product manager");
        }
        User newProductManager = foundProductManager.get();


        Optional<Product> foundProduct = productService.findProductById(id);
        if (!(foundProduct.isPresent())) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product could not be found");
        }
        else if (!(foundProduct.get().getProductManager().equals(user))) {
            // Automatically handled by Spring Boot; no need to implement an error controller
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Product is not owned by the user");
        }

        Product updatedProduct = productService.updateProductManager(id, newProductManager);
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }
}
