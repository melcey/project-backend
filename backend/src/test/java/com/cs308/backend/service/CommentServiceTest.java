package com.cs308.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cs308.backend.dao.Comment;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.CommentRepository;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllCommentsForProduct() {
        // Mock data
        Product product = new Product();
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        when(commentRepository.findByCommentedProduct(product)).thenReturn(Arrays.asList(comment1, comment2));

        // Call the service method
        List<Comment> comments = commentService.findAllCommentsForProduct(product);

        // Assert
        assertEquals(2, comments.size());
        verify(commentRepository, times(1)).findByCommentedProduct(product);
    }

    @Test
    public void testFindAllCommentsByUser() {
        // Mock data
        User user = new User();
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        when(commentRepository.findByCommentingUser(user)).thenReturn(Arrays.asList(comment1, comment2));

        // Call the service method
        List<Comment> comments = commentService.findAllCommentsByUser(user);

        // Assert
        assertEquals(2, comments.size());
        verify(commentRepository, times(1)).findByCommentingUser(user);
    }

    @Test
    public void testFindApprovedCommentsForProduct() {
        // Mock data
        Product product = new Product();
        Comment comment = new Comment();
        comment.setApproved(true);
        when(commentRepository.findByCommentedProductAndApproved(product, true)).thenReturn(Arrays.asList(comment));

        // Call the service method
        List<Comment> comments = commentService.findApprovedCommentsForProduct(product);

        // Assert
        assertEquals(1, comments.size());
        assertTrue(comments.get(0).getApproved());
        verify(commentRepository, times(1)).findByCommentedProductAndApproved(product, true);
    }

    @Test
    public void testFindApprovedCommentsByUser() {
        // Mock data
        User user = new User();
        Comment comment = new Comment();
        comment.setApproved(true);
        when(commentRepository.findByCommentingUserAndApproved(user, true)).thenReturn(Arrays.asList(comment));

        // Call the service method
        List<Comment> comments = commentService.findApprovedCommentsByUser(user);

        // Assert
        assertEquals(1, comments.size());
        assertTrue(comments.get(0).getApproved());
        verify(commentRepository, times(1)).findByCommentingUserAndApproved(user, true);
    }

    @Test
    public void testFindCommentById() {
        // Mock data
        Comment comment = new Comment();
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // Call the service method
        Optional<Comment> fetchedComment = commentService.findCommentById(1L);

        // Assert
        assertTrue(fetchedComment.isPresent());
        verify(commentRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindApprovedCommentById() {
        // Mock data
        Comment comment = new Comment();
        comment.setApproved(true);
        when(commentRepository.findByIdAndApproved(1L, true)).thenReturn(Optional.of(comment));

        // Call the service method
        Optional<Comment> fetchedComment = commentService.findApprovedCommentById(1L);

        // Assert
        assertTrue(fetchedComment.isPresent());
        assertTrue(fetchedComment.get().getApproved());
        verify(commentRepository, times(1)).findByIdAndApproved(1L, true);
    }

    @Test
    public void testSubmitComment() {
        // Mock data
        Comment comment = new Comment();
        when(commentRepository.save(comment)).thenReturn(comment);

        // Call the service method
        Optional<Comment> submittedComment = commentService.submitComment(comment);

        // Assert
        assertTrue(submittedComment.isPresent());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    public void testApproveComment() {
        // Mock data
        Comment comment = new Comment();
        when(commentRepository.save(comment)).thenReturn(comment);

        // Call the service method
        Optional<Comment> approvedComment = commentService.approveComment(comment);

        // Assert
        assertTrue(approvedComment.isPresent());
        assertTrue(approvedComment.get().getApproved());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    public void testDisapproveComment() {
        // Mock data
        Comment comment = new Comment();
        when(commentRepository.save(comment)).thenReturn(comment);

        // Call the service method
        Optional<Comment> disapprovedComment = commentService.disapproveComment(comment);

        // Assert
        assertTrue(disapprovedComment.isPresent());
        assertTrue(!disapprovedComment.get().getApproved());
        verify(commentRepository, times(1)).save(comment);
    }
}