package com.cs308.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.cs308.backend.dao.User;
import com.cs308.backend.dto.WishlistRequest;
import com.cs308.backend.dto.WishlistResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.WishlistService;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        return userDetails.getUser();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestBody WishlistRequest request) {
        wishlistService.addToWishlist(getCurrentUser(), request);
        return ResponseEntity.ok("Product added to wishlist.");
    }

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getWishlist() {
        List<WishlistResponse> wishlist = wishlistService.getWishlist(getCurrentUser());
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromWishlist(@PathVariable Long productId) {
        wishlistService.removeFromWishlist(getCurrentUser(), productId);
        return ResponseEntity.ok("Product removed from wishlist.");
    }
}
