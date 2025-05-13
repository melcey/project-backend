package com.cs308.backend.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cs308.backend.dao.Comment;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCommentingUser(User commentingUser);

    List<Comment> findByCommentingUserAndApproved(User commentingUser, boolean approved);

    List<Comment> findByApproved(boolean approved);

    List<Comment> findByCommentedProduct(Product commentedProduct);

    List<Comment> findByCommentedProductAndApproved(Product commentedProduct, boolean approved);

    Optional<Comment> findById(Long id);

    Optional<Comment> findByIdAndApproved(Long id, boolean approved);

    List<Comment> findByCommentDate(LocalDateTime commentDate);

    List<Comment> findByCommentDateAndApproved(LocalDateTime commentDate, boolean approved);

    List<Comment> findByCommentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Comment> findByApprovedAndCommentDateBetween(boolean approved, LocalDateTime startDate, LocalDateTime endDate);

    List<Comment> findByCommentDateBefore(LocalDateTime beforeDate);

    List<Comment> findByApprovedAndCommentDateBefore(boolean approved, LocalDateTime beforeDate);

    List<Comment> findByApprovedAndCommentDateAfter(boolean approved, LocalDateTime afterDate);

    List<Comment> findByApprovedFalse();
}
