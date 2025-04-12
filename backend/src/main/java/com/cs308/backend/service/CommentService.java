package com.cs308.backend.service;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Comment;
import com.cs308.backend.repo.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    Comment submitComment(Comment commentToSubmit) {
        Comment submittedComment = commentRepository.save(commentToSubmit);

        return submittedComment;
    }

    void approveComment(Comment commentToApprove) {
        if (!(commentToApprove.getIsApproved())) {
            commentToApprove.setIsApproved(true);
        }
    }

    void disapproveComment(Comment commentToDisapprove) {
        if (commentToDisapprove.getIsApproved()) {
            commentToDisapprove.setIsApproved(false);
        }
    }
    
    void deleteComment(Comment commentToDelete){
        commentRepository.delete(commentToDelete);
    }
}
