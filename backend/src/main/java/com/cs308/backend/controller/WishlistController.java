package com.cs308.backend.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dao.Wishlist;
import com.cs308.backend.dao.WishlistItem;
import com.cs308.backend.dto.CategoryResponse;
import com.cs308.backend.dto.ProductResponse;
import com.cs308.backend.dto.WishlistItemRequest;
import com.cs308.backend.dto.WishlistItemResponse;
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

        if ((authentication == null) || (!(authentication.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        User user = userDetails.getUser();
        
        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a sales manager");
        }
        
        return user;
    }

    @PostMapping
    public ResponseEntity<?> createWishlist() {
        User currentUser = getCurrentUser();

        Optional<Wishlist> createdWishlist = wishlistService.createWishlist(currentUser);

        if (!(createdWishlist.isPresent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wishlist creation failed");
        }

        Wishlist foundWishlist = createdWishlist.get();

        List<WishlistItemResponse> responseItems = new ArrayList<>();

        for (WishlistItem foundWishlistItem: foundWishlist.getWishlistItems()) {
            responseItems.add(new WishlistItemResponse(foundWishlistItem.getId(), new ProductResponse(foundWishlistItem.getProduct().getId(), foundWishlistItem.getProduct().getName(), foundWishlistItem.getProduct().getModel(), foundWishlistItem.getProduct().getSerialNumber(), foundWishlistItem.getProduct().getDescription(), foundWishlistItem.getProduct().getQuantityInStock(), foundWishlistItem.getProduct().getPrice(), foundWishlistItem.getProduct().getWarrantyStatus(), foundWishlistItem.getProduct().getDistributorInfo(), foundWishlistItem.getProduct().getIsActive(), foundWishlistItem.getProduct().getImageUrl(), new CategoryResponse(foundWishlistItem.getProduct().getCategory().getId(), foundWishlistItem.getProduct().getCategory().getName(), foundWishlistItem.getProduct().getCategory().getDescription()))));
        }

        WishlistResponse responseWishlist = new WishlistResponse(foundWishlist.getId(), currentUser.getId(), responseItems);

        return ResponseEntity.ok(responseWishlist);
    }

    @PutMapping("/add")
    public ResponseEntity<?> addToWishlist(@RequestBody WishlistItemRequest request) {
        User currentUser = getCurrentUser();

        Optional<Wishlist> updatedWishlist = wishlistService.addToWishlist(currentUser, request.getProductId());
        
        if (!(updatedWishlist.isPresent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item addition to wishlist failed");
        }

        Wishlist foundWishlist = updatedWishlist.get();

        List<WishlistItemResponse> responseItems = new ArrayList<>();

        for (WishlistItem foundWishlistItem: foundWishlist.getWishlistItems()) {
            responseItems.add(new WishlistItemResponse(foundWishlistItem.getId(), new ProductResponse(foundWishlistItem.getProduct().getId(), foundWishlistItem.getProduct().getName(), foundWishlistItem.getProduct().getModel(), foundWishlistItem.getProduct().getSerialNumber(), foundWishlistItem.getProduct().getDescription(), foundWishlistItem.getProduct().getQuantityInStock(), foundWishlistItem.getProduct().getPrice(), foundWishlistItem.getProduct().getWarrantyStatus(), foundWishlistItem.getProduct().getDistributorInfo(), foundWishlistItem.getProduct().getIsActive(), foundWishlistItem.getProduct().getImageUrl(), new CategoryResponse(foundWishlistItem.getProduct().getCategory().getId(), foundWishlistItem.getProduct().getCategory().getName(), foundWishlistItem.getProduct().getCategory().getDescription()))));
        }

        WishlistResponse responseWishlist = new WishlistResponse(foundWishlist.getId(), currentUser.getId(), responseItems);

        return ResponseEntity.ok(responseWishlist);
    }

    @GetMapping
    public ResponseEntity<?> getWishlist() {
        User currentUser = getCurrentUser();

        Optional<Wishlist> wishlist = wishlistService.getWishlist(currentUser);

        if (!(wishlist.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wishlist could not be found");
        }

        Wishlist foundWishlist = wishlist.get();

        List<WishlistItemResponse> responseItems = new ArrayList<>();

        for (WishlistItem foundWishlistItem: foundWishlist.getWishlistItems()) {
            responseItems.add(new WishlistItemResponse(foundWishlistItem.getId(), new ProductResponse(foundWishlistItem.getProduct().getId(), foundWishlistItem.getProduct().getName(), foundWishlistItem.getProduct().getModel(), foundWishlistItem.getProduct().getSerialNumber(), foundWishlistItem.getProduct().getDescription(), foundWishlistItem.getProduct().getQuantityInStock(), foundWishlistItem.getProduct().getPrice(), foundWishlistItem.getProduct().getWarrantyStatus(), foundWishlistItem.getProduct().getDistributorInfo(), foundWishlistItem.getProduct().getIsActive(), foundWishlistItem.getProduct().getImageUrl(), new CategoryResponse(foundWishlistItem.getProduct().getCategory().getId(), foundWishlistItem.getProduct().getCategory().getName(), foundWishlistItem.getProduct().getCategory().getDescription()))));
        }

        WishlistResponse responseWishlist = new WishlistResponse(foundWishlist.getId(), currentUser.getId(), responseItems);

        return ResponseEntity.ok(responseWishlist);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromWishlist(@RequestBody WishlistItemRequest request) {
        User currentUser = getCurrentUser();

        Optional<Wishlist> updatedWishlist = wishlistService.removeFromWishlist(currentUser, request.getProductId());
        
        if (!(updatedWishlist.isPresent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item deletion from wishlist failed");
        }

        Wishlist foundWishlist = updatedWishlist.get();

        List<WishlistItemResponse> responseItems = new ArrayList<>();

        for (WishlistItem foundWishlistItem: foundWishlist.getWishlistItems()) {
            responseItems.add(new WishlistItemResponse(foundWishlistItem.getId(), new ProductResponse(foundWishlistItem.getProduct().getId(), foundWishlistItem.getProduct().getName(), foundWishlistItem.getProduct().getModel(), foundWishlistItem.getProduct().getSerialNumber(), foundWishlistItem.getProduct().getDescription(), foundWishlistItem.getProduct().getQuantityInStock(), foundWishlistItem.getProduct().getPrice(), foundWishlistItem.getProduct().getWarrantyStatus(), foundWishlistItem.getProduct().getDistributorInfo(), foundWishlistItem.getProduct().getIsActive(), foundWishlistItem.getProduct().getImageUrl(), new CategoryResponse(foundWishlistItem.getProduct().getCategory().getId(), foundWishlistItem.getProduct().getCategory().getName(), foundWishlistItem.getProduct().getCategory().getDescription()))));
        }

        WishlistResponse responseWishlist = new WishlistResponse(foundWishlist.getId(), currentUser.getId(), responseItems);

        return ResponseEntity.ok(responseWishlist);
    }
}
