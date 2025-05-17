package com.cs308.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.dao.AnonCartItem;
import com.cs308.backend.dto.AnonCartItemRequest;
import com.cs308.backend.dto.AnonCartItemResponse;
import com.cs308.backend.dto.AnonCartResponse;
import com.cs308.backend.dto.CategoryResponse;
import com.cs308.backend.dto.ProductResponse;
import com.cs308.backend.service.AnonCartService;

@RestController
@RequestMapping("/anoncart")
public class AnonCartController {
    private final AnonCartService anonCartService;

    public AnonCartController(AnonCartService anonCartService) {
        this.anonCartService = anonCartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnonCart(@PathVariable Long id) {
        Optional<AnonCart> anonCart = anonCartService.getAnonCart(id);

        if (!(anonCart.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anonymous cart could not be found");
        }

        AnonCart retrievedAnonCart = anonCart.get();
        List<AnonCartItemResponse> responseItems = new ArrayList<>();

        for (AnonCartItem item: retrievedAnonCart.getItems()) {
            responseItems.add(new AnonCartItemResponse(item.getId(), retrievedAnonCart.getId(), new ProductResponse(item.getProduct().getId(), item.getProduct().getName(), item.getProduct().getModel(), item.getProduct().getSerialNumber(), item.getProduct().getDescription(), item.getProduct().getQuantityInStock(), item.getProduct().getPrice(), item.getProduct().getWarrantyStatus(), item.getProduct().getDistributorInfo(), item.getProduct().getIsActive(), item.getProduct().getImageUrl(), new CategoryResponse(item.getProduct().getCategory().getId(), item.getProduct().getCategory().getName(), item.getProduct().getCategory().getDescription())), item.getQuantity(), item.getPriceAtAddition()));
        }

        return ResponseEntity.ok(new AnonCartResponse(retrievedAnonCart.getId(), retrievedAnonCart.getTotalPrice(), responseItems));
    }
    
    @PostMapping
    public ResponseEntity<?> createAnonCart() {
        Optional<AnonCart> createdAnonCart = anonCartService.createAnonCart();

        if (!(createdAnonCart.isPresent())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The anonymous cart could not be created");
        }

        AnonCart newAnonCart = createdAnonCart.get();

        return ResponseEntity.ok(new AnonCartResponse(newAnonCart.getId(), newAnonCart.getTotalPrice(), new ArrayList<>()));
    }

    @PutMapping("/{id}/add")
    public ResponseEntity<?> addItemToAnonCart(@PathVariable Long id, @RequestBody AnonCartItemRequest request) {
        Optional<AnonCart> anonCart = anonCartService.addItemToAnonCart(id, request.getProductId(), request.getQuantity());

        if (!(anonCart.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anonymous cart could not be found");
        }

        AnonCart updatedAnonCart = anonCart.get();
        List<AnonCartItemResponse> responseItems = new ArrayList<>();

        for (AnonCartItem item: updatedAnonCart.getItems()) {
            responseItems.add(new AnonCartItemResponse(item.getId(), updatedAnonCart.getId(), new ProductResponse(item.getProduct().getId(), item.getProduct().getName(), item.getProduct().getModel(), item.getProduct().getSerialNumber(), item.getProduct().getDescription(), item.getProduct().getQuantityInStock(), item.getProduct().getPrice(), item.getProduct().getWarrantyStatus(), item.getProduct().getDistributorInfo(), item.getProduct().getIsActive(), item.getProduct().getImageUrl(), new CategoryResponse(item.getProduct().getCategory().getId(), item.getProduct().getCategory().getName(), item.getProduct().getCategory().getDescription())), item.getQuantity(), item.getPriceAtAddition()));
        }

        return ResponseEntity.ok(new AnonCartResponse(updatedAnonCart.getId(), updatedAnonCart.getTotalPrice(), responseItems));
    }

    @PutMapping("/{id}/delete")
    public ResponseEntity<?> deleteItemFromAnonCart(@PathVariable Long id, @RequestBody AnonCartItemRequest request) {
        Optional<AnonCart> anonCart = anonCartService.deleteItemFromAnonCart(id, request.getProductId(), request.getQuantity());

        if (!(anonCart.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anonymous cart could not be found");
        }

        AnonCart updatedAnonCart = anonCart.get();
        List<AnonCartItemResponse> responseItems = new ArrayList<>();

        for (AnonCartItem item: updatedAnonCart.getItems()) {
            responseItems.add(new AnonCartItemResponse(item.getId(), updatedAnonCart.getId(), new ProductResponse(item.getProduct().getId(), item.getProduct().getName(), item.getProduct().getModel(), item.getProduct().getSerialNumber(), item.getProduct().getDescription(), item.getProduct().getQuantityInStock(), item.getProduct().getPrice(), item.getProduct().getWarrantyStatus(), item.getProduct().getDistributorInfo(), item.getProduct().getIsActive(), item.getProduct().getImageUrl(), new CategoryResponse(item.getProduct().getCategory().getId(), item.getProduct().getCategory().getName(), item.getProduct().getCategory().getDescription())), item.getQuantity(), item.getPriceAtAddition()));
        }

        return ResponseEntity.ok(new AnonCartResponse(updatedAnonCart.getId(), updatedAnonCart.getTotalPrice(), responseItems));
    }
}