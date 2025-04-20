package com.cs308.backend.controller;

import com.cs308.backend.dao.Comment;
import com.cs308.backend.dto.MessageResponse;
import com.cs308.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitComment(@RequestBody Comment comment) {
        // Get authenticated user if needed:
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // UserPrincipal user = (UserPrincipal) auth.getPrincipal(); // If using a
        // custom UserPrincipal

        Comment submitted = commentService.submitComment(comment);
        return ResponseEntity.ok(submitted);
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approveComment(@RequestBody Comment comment) {
        commentService.approveComment(comment);
        return ResponseEntity.ok(new MessageResponse("Comment approved successfully."));
    }

    @PostMapping("/disapprove")
    public ResponseEntity<?> disapproveComment(@RequestBody Comment comment) {
        commentService.disapproveComment(comment);
        return ResponseEntity.ok(new MessageResponse("Comment disapproved successfully."));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestBody Comment comment) {
        commentService.deleteComment(comment);
        return ResponseEntity.ok(new MessageResponse("Comment deleted successfully."));
    }
}
