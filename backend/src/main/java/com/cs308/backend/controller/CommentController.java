package com.cs308.backend.controller;

import com.cs308.backend.dao.Comment;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.MessageResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitComment(@RequestBody Comment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only customers can submit comments.");
        }

        comment.setCommentingUser(user);

        Comment submitted = commentService.submitComment(comment);
        return ResponseEntity.ok(submitted);
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approveComment(@RequestBody Comment comment) {
        authorize();
        commentService.approveComment(comment);
        return ResponseEntity.ok(new MessageResponse("Comment approved successfully."));
    }

    @PostMapping("/disapprove")
    public ResponseEntity<?> disapproveComment(@RequestBody Comment comment) {
        authorize();
        commentService.disapproveComment(comment);
        return ResponseEntity.ok(new MessageResponse("Comment disapproved successfully."));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestBody Comment comment) {
        authorize();
        commentService.deleteComment(comment);
        return ResponseEntity.ok(new MessageResponse("Comment deleted successfully."));
    }

    private void authorize() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
        User user = userDetails.getUser();

        if (user.getRole() != Role.sales_manager && user.getRole() != Role.product_manager) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }
    }
}
