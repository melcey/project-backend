package com.cs308.backend.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cs308.backend.dao.OrderStatus;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Rating;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.CategoryResponse;
import com.cs308.backend.dto.ProductResponse;
import com.cs308.backend.dto.RatingResponse;
import com.cs308.backend.dto.SubmitRatingRequest;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.OrderService;
import com.cs308.backend.service.ProductService;
import com.cs308.backend.service.RatingService;

@RestController
@RequestMapping("/rating")
public class RatingController {

    private final RatingService ratingService;
    private final ProductService productService;
    private final OrderService orderService;

    public RatingController(RatingService ratingService, ProductService productService, OrderService orderService) {
        this.ratingService = ratingService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitRating(@RequestBody SubmitRatingRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only customers can rate products");
        }

        Optional<Product> productOpt = productService.findProductById(request.getProductId());
        if (productOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }

        Product product = productOpt.get();

        boolean hasOrderedAndDelivered = orderService.findAllOrdersIncludingProduct(product).stream()
            .anyMatch(order -> order.getUser().equals(user) && order.getStatus().equals(OrderStatus.delivered));

        if (!hasOrderedAndDelivered) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The customer did not order this product.");
        }

        // Update rating if user has already rated this product

        Optional<Rating> existingRatingOpt = ratingService
            .findRatingsForProduct(product)
            .stream()
            .filter(r -> r.getRatingUser().getId().equals(user.getId()))
            .findFirst();

        Rating rating;
        if (existingRatingOpt.isPresent()) {
            rating = existingRatingOpt.get();
            rating.setRating(request.getRatingValue());
            rating.setRatingDate(java.time.LocalDateTime.now());
        } else {
            rating = new Rating(product, user, request.getRatingValue());
        }

        Optional<Rating> submittedRating = ratingService.submitRating(rating);

        if (!(submittedRating.isPresent())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Rating submission failed");
        }

        Rating newRating = submittedRating.get();

        return ResponseEntity.ok(new RatingResponse(newRating.getId(), new ProductResponse(newRating.getRatedProduct().getId(), newRating.getRatedProduct().getName(), newRating.getRatedProduct().getModel(), newRating.getRatedProduct().getSerialNumber(), newRating.getRatedProduct().getDescription(), newRating.getRatedProduct().getQuantityInStock(), newRating.getRatedProduct().getPrice(), newRating.getRatedProduct().getWarrantyStatus(), newRating.getRatedProduct().getDistributorInfo(), newRating.getRatedProduct().getIsActive(), newRating.getRatedProduct().getImageUrl(), new CategoryResponse(newRating.getRatedProduct().getCategory().getId(), newRating.getRatedProduct().getCategory().getName(), newRating.getRatedProduct().getCategory().getDescription())), user.getId(), newRating.getRating(), newRating.getRatingDate()));
    }
}

