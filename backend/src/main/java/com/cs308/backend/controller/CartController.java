package com.cs308.backend.controller;

import com.cs308.backend.dao.Cart;
import com.cs308.backend.service.CartService;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestParam Long userId) {
        Optional<Cart> retrievedCart = cartService.getCartOfUser(userId);

        if (!(retrievedCart.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such cart");
        }

        return ResponseEntity.ok(retrievedCart.get());
    }

    @PutMapping("/add")
    public ResponseEntity<Cart> addItemToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, productId, quantity));
    }
}