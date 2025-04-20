package com.cs308.backend.controller;

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.service.AnonCartService;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/anoncart")
public class AnonCartController {
    private final AnonCartService anonCartService;

    public AnonCartController(AnonCartService anonCartService) {
        this.anonCartService = anonCartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnonCart> getAnonCart(@PathVariable Long id) {
        Optional<AnonCart> anonCart = anonCartService.getAnonCart(id);

        if (!(anonCart.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anonymous cart could not be found");
        }

        return ResponseEntity.ok(anonCart.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnonCart> addItemToAnonCart(@PathVariable Long id, @RequestParam Long productId, @RequestParam int quantity) {
        Optional<AnonCart> anonCart = anonCartService.addItemToAnonCart(id, productId, quantity);

        if (!(anonCart.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Anonymous cart could not be found");
        }

        return ResponseEntity.ok(anonCart.get());
    }
}