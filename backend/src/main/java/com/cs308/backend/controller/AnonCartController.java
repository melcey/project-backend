package com.cs308.backend.controller;

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.service.AnonCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/anoncart")
public class AnonCartController {
    private final AnonCartService anonCartService;

    public AnonCartController(AnonCartService anonCartService) {
        this.anonCartService = anonCartService;
    }


    @GetMapping
    public ResponseEntity<AnonCart> getAnonCart(@RequestParam Long userId) {
        return ResponseEntity.ok(anonCartService.getOrCreateAnonCart(userId));
    }

    @PutMapping("/add")
    public ResponseEntity<AnonCart> addItemToAnonCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(anonCartService.addItemToAnonCart(userId, productId, quantity));
    }
}