package com.cs308.backend.service;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Comment;
import com.cs308.backend.repo.CommentRepository;

@Service
public class CommentService {
    public final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment submitComment(Comment commentToSubmit) {
        Comment submittedComment = commentRepository.save(commentToSubmit);

        return submittedComment;
    }

    public void approveComment(Comment commentToApprove) {
        if (!(commentToApprove.getIsApproved())) {
            commentToApprove.setIsApproved(true);
        }
    }

    public void disapproveComment(Comment commentToDisapprove) {
        if (commentToDisapprove.getIsApproved()) {
            commentToDisapprove.setIsApproved(false);
        }
    }
    
    public void deleteComment(Comment commentToDelete){
        commentRepository.delete(commentToDelete);
    }
}
