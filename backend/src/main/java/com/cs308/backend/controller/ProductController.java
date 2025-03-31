package com.cs308.backend.controller;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Retrieve a product by its ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        Optional<Product> productOpt = productService.findProductById(id);
        return productOpt.orElse(null);
    }

    // Turn this into a unified search endpoint
    // Retrieve products by name (including full name and containing)
    // For example: GET /products?name=Widget
    @GetMapping
    public List<Product> getProducts(@RequestParam(required = false) String name) {
        if (name != null) {
            return productService.findProductsByName(name);
        }
        
        // Returns an empty ArrayList if the name is null
        return new ArrayList<>();
    }

    // Create a new product
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
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

        product.setProductManager(user);
        return productService.createProduct(product);
    }

    // Delete a product by its id
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
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
    }

    // Update endpoints for individual fields

    // Update product name
    @PutMapping("/{id}/name")
    public Product updateProductName(@PathVariable Long id, @RequestBody String newName) {
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

        return productService.updateProductName(id, newName);
    }

    // Update product model
    @PutMapping("/{id}/model")
    public Product updateProductModel(@PathVariable Long id, @RequestBody String newModel) {
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

        return productService.updateProductModel(id, newModel);
    }

    // Update product serial number
    @PutMapping("/{id}/serialNumber")
    public Product updateProductSerialNumber(@PathVariable Long id, @RequestBody String newSerialNumber) {
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

        return productService.updateProductSerialNumber(id, newSerialNumber);
    }

    // Update product description
    @PutMapping("/{id}/description")
    public Product updateProductDescription(@PathVariable Long id, @RequestBody String newDescription) {
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

        return productService.updateProductDescription(id, newDescription);
    }

    // Update product stock quantity
    @PutMapping("/{id}/quantity")
    public Product updateProductQuantity(@PathVariable Long id, @RequestBody int newQuantity) {
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
        
        return productService.updateProductQuantityInStock(id, newQuantity);
    }

    // Update product price
    @PutMapping("/{id}/price")
    public Product updateProductPrice(@PathVariable Long id, @RequestBody BigDecimal newPrice) {
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

        return productService.updateProductPrice(id, newPrice);
    }

    // Update warranty status
    @PutMapping("/{id}/warrantyStatus")
    public Product updateProductWarrantyStatus(@PathVariable Long id, @RequestBody String newWarrantyStatus) {
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

        return productService.updateProductWarrantyStatus(id, newWarrantyStatus);
    }
    
    // Update distributor info
    @PutMapping("/{id}/distributorInfo")
    public Product updateProductDistributorInfo(@PathVariable Long id, @RequestBody String newDistributorInfo) {
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

        return productService.updateProductDistributorInfo(id, newDistributorInfo);
    }
    
    // Update active status
    @PutMapping("/{id}/isActive")
    public Product updateProductIsActive(@PathVariable Long id, @RequestBody Boolean newIsActive) {
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

        return productService.updateProductIsActive(id, newIsActive);
    }
    
    // Update category
    @PutMapping("/{id}/category")
    public Product updateProductCategory(@PathVariable Long id, @RequestBody String newCategoryName) {
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

        return productService.updateProductCategory(id, newCategoryName);
    }
    
    // Update product manager
    @PutMapping("/{id}/productManager")
    public Product updateProductManager(@PathVariable Long id, @RequestBody User newProductManager) {
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

        return productService.updateProductManager(id, newProductManager);
    }
}