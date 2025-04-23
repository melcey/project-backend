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

import com.cs308.backend.dao.Comment;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.CategoryResponse;
import com.cs308.backend.dto.CommentRequest;
import com.cs308.backend.dto.CommentResponse;
import com.cs308.backend.dto.CommentStateRequest;
import com.cs308.backend.dto.MessageResponse;
import com.cs308.backend.dto.ProductResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.CommentService;
import com.cs308.backend.service.ProductService;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final ProductService productService;

    public CommentController(CommentService commentService, ProductService productService) {
        this.commentService = commentService;
        this.productService = productService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitComment(@RequestBody CommentRequest commentRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only customers can submit comments.");
        }
        
        Optional<Product> productToComment = productService.findProductById(commentRequest.getProductId());

        if (!(productToComment.isPresent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The product does not exist");
        }

        Optional<Comment> submittedComment = commentService.submitComment(new Comment(productToComment.get(), user, commentRequest.getComment()));

        if (!(submittedComment.isPresent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment submission failed.");
        }

        return ResponseEntity.ok(new MessageResponse("The comment is submitted"));
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approveComment(@RequestBody CommentStateRequest commentStateRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Comment> commentToApprove = commentService.findCommentById(commentStateRequest.getCommentId());

        if (!(commentToApprove.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such comment exists");
        }

        if (!(commentToApprove.get().getCommentedProduct().getProductManager().equals(user))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Comment> comment = commentService.approveComment(commentToApprove.get());

        if (!(comment.isPresent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment approval failed");
        }

        Comment approvedComment = comment.get();
        
        return ResponseEntity.ok(new CommentResponse(approvedComment.getId(), new ProductResponse(approvedComment.getCommentedProduct().getId(), approvedComment.getCommentedProduct().getName(), approvedComment.getCommentedProduct().getModel(), approvedComment.getCommentedProduct().getSerialNumber(), approvedComment.getCommentedProduct().getDescription(), approvedComment.getCommentedProduct().getQuantityInStock(), approvedComment.getCommentedProduct().getPrice(), approvedComment.getCommentedProduct().getWarrantyStatus(), approvedComment.getCommentedProduct().getDistributorInfo(), approvedComment.getCommentedProduct().getIsActive(), approvedComment.getCommentedProduct().getImageUrl(), new CategoryResponse(approvedComment.getCommentedProduct().getCategory().getId(), approvedComment.getCommentedProduct().getCategory().getName(), approvedComment.getCommentedProduct().getCategory().getDescription())), approvedComment.getCommentingUser().getId(), approvedComment.getApproved(), approvedComment.getComment(), approvedComment.getCommentDate()));
    }

    @PostMapping("/disapprove")
    public ResponseEntity<?> disapproveComment(@RequestBody CommentStateRequest commentStateRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
        User user = userDetails.getUser();

        if (user.getRole() != Role.product_manager) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Comment> commentToDisapprove = commentService.findCommentById(commentStateRequest.getCommentId());

        if (!(commentToDisapprove.isPresent())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such comment exists");
        }

        if (!(commentToDisapprove.get().getCommentedProduct().getProductManager().equals(user))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Comment> comment = commentService.disapproveComment(commentToDisapprove.get());

        if (!(comment.isPresent())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment disapproval failed");
        }

        Comment disapprovedComment = comment.get();
        
        return ResponseEntity.ok(new CommentResponse(disapprovedComment.getId(), new ProductResponse(disapprovedComment.getCommentedProduct().getId(), disapprovedComment.getCommentedProduct().getName(), disapprovedComment.getCommentedProduct().getModel(), disapprovedComment.getCommentedProduct().getSerialNumber(), disapprovedComment.getCommentedProduct().getDescription(), disapprovedComment.getCommentedProduct().getQuantityInStock(), disapprovedComment.getCommentedProduct().getPrice(), disapprovedComment.getCommentedProduct().getWarrantyStatus(), disapprovedComment.getCommentedProduct().getDistributorInfo(), disapprovedComment.getCommentedProduct().getIsActive(), disapprovedComment.getCommentedProduct().getImageUrl(), new CategoryResponse(disapprovedComment.getCommentedProduct().getCategory().getId(), disapprovedComment.getCommentedProduct().getCategory().getName(), disapprovedComment.getCommentedProduct().getCategory().getDescription())), disapprovedComment.getCommentingUser().getId(), disapprovedComment.getApproved(), disapprovedComment.getComment(), disapprovedComment.getCommentDate()));
    }
}
