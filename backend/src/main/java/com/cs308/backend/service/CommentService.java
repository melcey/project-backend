package com.cs308.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Comment;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.CommentRepository;

@Service
public class CommentService {
    public final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findAllCommentsForProduct(Product product) {
        return commentRepository.findByCommentedProduct(product);
    }

    public List<Comment> findAllCommentsByUser(User user) {
        return commentRepository.findByCommentingUser(user);
    }

    public List<Comment> findApprovedCommentsForProduct(Product product) {
        return commentRepository.findByCommentedProductAndApproved(product, true);
    }

    public List<Comment> findApprovedCommentsByUser(User user) {
        return commentRepository.findByCommentingUserAndApproved(user, true);
    }

    public Optional<Comment> findCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public Optional<Comment> findApprovedCommentById(Long id) {
        return commentRepository.findByIdAndApproved(id, true);
    }

    public Optional<Comment> submitComment(Comment commentToSubmit) {
        try {
            Comment submittedComment = commentRepository.save(commentToSubmit);
            return Optional.of(submittedComment);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Comment> approveComment(Comment commentToApprove) {
        commentToApprove.setApproved(true);

        try {
            Comment approvedComment = commentRepository.save(commentToApprove);
            return Optional.of(approvedComment);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Comment> disapproveComment(Comment commentToDisapprove) {
        commentToDisapprove.setApproved(false);

        try {
            Comment disapprovedComment = commentRepository.save(commentToDisapprove);
            return Optional.of(disapprovedComment);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public ResponseEntity<List<Comment>> getPendingComments() {
        try {
            List<Comment> pendingComments = commentRepository.findByApprovedFalse();
            return ResponseEntity.ok(pendingComments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public ResponseEntity<?> approveComment(Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Comment not found"));

            comment.setApproved(true);
            Comment updatedComment = commentRepository.save(comment);
            return ResponseEntity.ok(updatedComment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error approving comment: " + e.getMessage());
        }
    }

    public ResponseEntity<?> disapproveComment(Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new RuntimeException("Comment not found"));

            comment.setApproved(false);
            Comment updatedComment = commentRepository.save(comment);
            return ResponseEntity.ok(updatedComment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error disapproving comment: " + e.getMessage());
        }
    }
}
