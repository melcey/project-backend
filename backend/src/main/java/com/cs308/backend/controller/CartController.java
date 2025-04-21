package com.cs308.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cs308.backend.dao.Cart;
import com.cs308.backend.dao.CartItem;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.AddCartItemRequest;
import com.cs308.backend.dto.CartItemResponse;
import com.cs308.backend.dto.CartResponse;
import com.cs308.backend.dto.CategoryResponse;
import com.cs308.backend.dto.ProductResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<?> getCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Cart> cart = cartService.getCartOfUser(user);

        if (!(cart.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such cart");
        }

        Cart retrievedCart = cart.get();
        List<CartItemResponse> responseItems = new ArrayList<>();

        for (CartItem item: retrievedCart.getItems()) {
            responseItems.add(new CartItemResponse(item.getId(), retrievedCart.getId(), new ProductResponse(item.getProduct().getId(), item.getProduct().getName(), item.getProduct().getModel(), item.getProduct().getSerialNumber(), item.getProduct().getDescription(), item.getProduct().getQuantityInStock(), item.getProduct().getPrice(), item.getProduct().getWarrantyStatus(), item.getProduct().getDistributorInfo(), item.getProduct().getIsActive(), item.getProduct().getImageUrl(), new CategoryResponse(item.getProduct().getCategory().getId(), item.getProduct().getCategory().getName(), item.getProduct().getCategory().getDescription())), item.getQuantity(), item.getPriceAtAddition()));
        }

        return ResponseEntity.ok(new CartResponse(retrievedCart.getId(), user.getId(), retrievedCart.getTotalPrice(), responseItems));
    }

    @PostMapping
    public ResponseEntity<?> createCartFromAnonCart(@RequestParam Long anonCartId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Cart> cart = cartService.anonCartToCart(anonCartId, user);

        if (!(cart.isPresent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart creation failed");
        }

        Cart createdCart = cart.get();
        List<CartItemResponse> responseItems = new ArrayList<>();

        for (CartItem item: createdCart.getItems()) {
            responseItems.add(new CartItemResponse(item.getId(), createdCart.getId(), new ProductResponse(item.getProduct().getId(), item.getProduct().getName(), item.getProduct().getModel(), item.getProduct().getSerialNumber(), item.getProduct().getDescription(), item.getProduct().getQuantityInStock(), item.getProduct().getPrice(), item.getProduct().getWarrantyStatus(), item.getProduct().getDistributorInfo(), item.getProduct().getIsActive(), item.getProduct().getImageUrl(), new CategoryResponse(item.getProduct().getCategory().getId(), item.getProduct().getCategory().getName(), item.getProduct().getCategory().getDescription())), item.getQuantity(), item.getPriceAtAddition()));
        }

        return ResponseEntity.ok(new CartResponse(createdCart.getId(), user.getId(), createdCart.getTotalPrice(), responseItems));
    }

    @PutMapping("/add")
    public ResponseEntity<?> addItemToCart(@RequestBody AddCartItemRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Cart> cart = cartService.addItemToCart(user, request.getProductId(), request.getQuantity());

        if (!(cart.isPresent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item addition to cart failed");
        }

        Cart updatedCart = cart.get();
        List<CartItemResponse> responseItems = new ArrayList<>();

        for (CartItem item: updatedCart.getItems()) {
            responseItems.add(new CartItemResponse(item.getId(), updatedCart.getId(), new ProductResponse(item.getProduct().getId(), item.getProduct().getName(), item.getProduct().getModel(), item.getProduct().getSerialNumber(), item.getProduct().getDescription(), item.getProduct().getQuantityInStock(), item.getProduct().getPrice(), item.getProduct().getWarrantyStatus(), item.getProduct().getDistributorInfo(), item.getProduct().getIsActive(), item.getProduct().getImageUrl(), new CategoryResponse(item.getProduct().getCategory().getId(), item.getProduct().getCategory().getName(), item.getProduct().getCategory().getDescription())), item.getQuantity(), item.getPriceAtAddition()));
        }

        return ResponseEntity.ok(new CartResponse(updatedCart.getId(), user.getId(), updatedCart.getTotalPrice(), responseItems));
    }
}