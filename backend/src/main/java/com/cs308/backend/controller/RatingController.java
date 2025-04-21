package com.cs308.backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Rating;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.MessageResponse;
import com.cs308.backend.repo.ProductRepository;
import com.cs308.backend.repo.RatingRepository;
import com.cs308.backend.security.UserPrincipal;

@RestController
@RequestMapping("/rating")
public class RatingController {

    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;

    public RatingController(RatingRepository ratingRepository, ProductRepository productRepository) {
        this.ratingRepository = ratingRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitRating(@RequestParam Long productId, @RequestParam int ratingValue) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only customers can rate products");
        }

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        Product product = productOpt.get();

        // Update rating if user has already rated this product

        Optional<Rating> existingRatingOpt = ratingRepository
            .findAll()
            .stream()
            .filter(r -> r.getRatedProduct().getId().equals(productId) && r.getRatedUser().getId().equals(user.getId()))
            .findFirst();

        Rating rating;
        if (existingRatingOpt.isPresent()) {
            rating = existingRatingOpt.get();
            rating.setRating(ratingValue);
            rating.setRatingDate(java.time.LocalDateTime.now());
        } else {
            rating = new Rating(product, user, ratingValue);
        }

        ratingRepository.save(rating);

        return ResponseEntity.ok(new MessageResponse("Rating submitted successfully."));
    }
}

