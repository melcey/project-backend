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
import com.cs308.backend.service.ProductManagerActionService;
import com.cs308.backend.service.ProductService;
import com.cs308.backend.service.UserService;

@RestController
@RequestMapping("/prodman")
public class ProductManagerController {
    private final ProductService productService;
    private final UserService userService;
    private final ProductManagerActionService actionService;

    public ProductManagerController(ProductService productService, UserService userService, ProductManagerActionService actionService) {
        this.productService = productService;
        this.userService = userService;
        this.actionService = actionService;
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
    // Example use for category search:
    //GET /prodman?categoryId=1&categoryId=2&categoryId=3
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
            @RequestParam(required = false) Integer maxQuantity,
            @RequestParam(required = false) List<Long> categoryId) {

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
            (minQuantity == null) && (maxQuantity == null) && ((categoryId == null) || (categoryId.isEmpty()))) {
            return ResponseEntity.ok(new ProductListResponse());
        }

        List<Product> foundProducts = productService.searchManagedProducts(user, name, model, serialNumber, description, distributorInfo, isActive,
                warrantyStatus, minPrice, maxPrice, minQuantity, maxQuantity, categoryId);
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

        actionService.logAction(user, "CREATE_PRODUCT", Long.toString(createdProduct.getId()));

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
        actionService.logAction(user, "DELETE_PRODUCT", Long.toString(id));

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
        else if ((updateProductRequest.getName() == null) && (updateProductRequest.getModel() == null) && (updateProductRequest.getSerialNumber() == null) && (updateProductRequest.getDescription() == null) && (updateProductRequest.getQuantityInStock() == null) && ((updateProductRequest.getPrice() == null)) && (updateProductRequest.getWarrantyStatus() == null) && (updateProductRequest.getDistributorInfo() == null) && (updateProductRequest.getIsActive() == null) && (updateProductRequest.getCategoryId() == null)) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "No updates to the product");
        }

        if (updateProductRequest.getName() != null) {
            String oldName = product.getName();
            product = productService.updateProductName(id, updateProductRequest.getName());
            actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", product.getId(), oldName, updateProductRequest.getName()));
        }

        if (updateProductRequest.getModel() != null) {
            String oldModel = product.getModel();
            product = productService.updateProductModel(id, updateProductRequest.getModel());
            actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", product.getId(), oldModel, updateProductRequest.getModel()));
        }

        if (updateProductRequest.getSerialNumber() != null) {
            String oldSerialNumber = product.getSerialNumber();
            product = productService.updateProductSerialNumber(id, updateProductRequest.getSerialNumber());
            actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", product.getId(), oldSerialNumber, updateProductRequest.getSerialNumber()));
        }

        if (updateProductRequest.getDescription() != null) {
            String oldDescription = product.getDescription();
            product = productService.updateProductDescription(id, updateProductRequest.getDescription());
            actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", product.getId(), oldDescription, updateProductRequest.getDescription()));
        }

        if (updateProductRequest.getQuantityInStock() != null) {
            Integer oldQuantityInStock = product.getQuantityInStock();
            product = productService.updateProductQuantityInStock(id, updateProductRequest.getQuantityInStock());
            actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %d -> %d", product.getId(), oldQuantityInStock.intValue(), updateProductRequest.getQuantityInStock().intValue()));
        }

        if (updateProductRequest.getPrice() != null) {
            BigDecimal oldPrice = product.getPrice();
            product = productService.updateProductPrice(id, updateProductRequest.getPrice());
            actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", product.getId(), oldPrice.toString(), updateProductRequest.getPrice().toString()));
        }

        if (updateProductRequest.getWarrantyStatus() != null) {
            String oldWarrantyStatus = product.getWarrantyStatus();
            product = productService.updateProductWarrantyStatus(id, updateProductRequest.getWarrantyStatus());
            actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", product.getId(), oldWarrantyStatus, updateProductRequest.getWarrantyStatus()));
        }

        if (updateProductRequest.getDistributorInfo() != null) {
            String oldDistributorInfo = product.getDistributorInfo();
            product = productService.updateProductDistributorInfo(id, updateProductRequest.getDistributorInfo());
            actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", product.getId(), oldDistributorInfo, updateProductRequest.getDistributorInfo()));
        }

        if (updateProductRequest.getIsActive() != null) {
            boolean oldIsActive = product.getIsActive();
            product = productService.updateProductIsActive(id, updateProductRequest.getIsActive());
            actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %b -> %b", product.getId(), oldIsActive, updateProductRequest.getIsActive()));
        }

        if (updateProductRequest.getImageUrl() != null) {
            String oldImageUrl = product.getImageUrl();
            product = productService.updateProductImageUrl(id, updateProductRequest.getImageUrl());
            actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", product.getId(), oldImageUrl, updateProductRequest.getImageUrl()));
        }

        if (updateProductRequest.getCategoryId() != null) {
            String oldCategory = product.getCategory().toString();
            product = productService.updateProductCategory(id, updateProductRequest.getCategoryId());
            actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", product.getId(), oldCategory, product.getCategory().toString()));
        }

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

        String oldName = foundProduct.get().getName();
        Product updatedProduct = productService.updateProductName(id, newName);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", updatedProduct.getId(), oldName, updatedProduct.getName()));
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

        String oldModel = foundProduct.get().getModel();
        Product updatedProduct = productService.updateProductModel(id, newModel);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", updatedProduct.getId(), oldModel, updatedProduct.getModel()));
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

        String oldSerialNumber = foundProduct.get().getSerialNumber();
        Product updatedProduct = productService.updateProductSerialNumber(id, newSerialNumber);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", updatedProduct.getId(), oldSerialNumber, updatedProduct.getSerialNumber()));
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

        String oldDescription = foundProduct.get().getDescription();
        Product updatedProduct = productService.updateProductDescription(id, newDescription);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", updatedProduct.getId(), oldDescription, updatedProduct.getDescription()));
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
        
        Integer oldQuantityInStock = foundProduct.get().getQuantityInStock();
        Product updatedProduct = productService.updateProductQuantityInStock(id, newQuantity);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %d -> %d", updatedProduct.getId(), oldQuantityInStock.intValue(), updatedProduct.getQuantityInStock()));
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

        BigDecimal oldPrice = foundProduct.get().getPrice();
        Product updatedProduct = productService.updateProductPrice(id, newPrice);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", updatedProduct.getId(), oldPrice.toString(), updatedProduct.getPrice().toString()));
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

        String oldWarrantyStatus = foundProduct.get().getWarrantyStatus();
        Product updatedProduct = productService.updateProductWarrantyStatus(id, newWarrantyStatus);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", updatedProduct.getId(), oldWarrantyStatus, updatedProduct.getWarrantyStatus()));
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

        String oldDistributorInfo = foundProduct.get().getDistributorInfo();
        Product updatedProduct = productService.updateProductDistributorInfo(id, newDistributorInfo);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", updatedProduct.getId(), oldDistributorInfo, updatedProduct.getDistributorInfo()));
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

        boolean oldIsActive = foundProduct.get().getIsActive();
        Product updatedProduct = productService.updateProductIsActive(id, true);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %b -> %b", updatedProduct.getId(), oldIsActive, updatedProduct.getIsActive()));
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

        boolean oldIsActive = foundProduct.get().getIsActive();
        Product updatedProduct = productService.updateProductIsActive(id, false);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %b -> %b", updatedProduct.getId(), oldIsActive, updatedProduct.getIsActive()));
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }

    // Update image URL
    @PutMapping("/{id}/img")
    public ResponseEntity<?> updateProductImageUrl(@PathVariable Long id, @RequestBody String newImageUrl) {
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

        String oldImageUrl = foundProduct.get().getImageUrl();
        Product updatedProduct = productService.updateProductImageUrl(id, newImageUrl);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", updatedProduct.getId(), oldImageUrl, updatedProduct.getImageUrl()));
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }

    
    // Update category
    @PutMapping("/{id}/category")
    public ResponseEntity<?> updateProductCategory(@PathVariable Long id, @RequestBody Long newCategoryId) {
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

        String oldCategory = foundProduct.get().getCategory().toString();
        Product updatedProduct = productService.updateProductCategory(id, newCategoryId);
        actionService.logAction(user, "UPDATE_PRODUCT", String.format("%d: %s -> %s", updatedProduct.getId(), oldCategory, updatedProduct.getCategory().toString()));
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

        String oldProductManager = foundProduct.get().getProductManager().toString();
        Product updatedProduct = productService.updateProductManager(id, newProductManager);
        actionService.logAction(user, "CHANGE_PRODUCTMANAGER", String.format("%d: %s -> %s", updatedProduct.getId(), oldProductManager, updatedProduct.getProductManager().toString()));
        return ResponseEntity.ok(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
    }
}
