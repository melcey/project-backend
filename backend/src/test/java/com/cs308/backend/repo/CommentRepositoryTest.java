package com.cs308.backend.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cs308.backend.dao.Comment;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.dao.Role;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
public class CommentRepositoryTest {
    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
        .withDatabaseName("testdb")
        .withUsername("testuser")
        .withPassword("testpass");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindByCommentingUser() {
        // Create a User
        User user = new User("John Doe", "123 Main St", Role.customer);
        User savedUser = userRepository.save(user);

        // Create a Comment
        Comment comment = new Comment();
        comment.setCommentingUser(savedUser);
        comment.setComment("Great product!");
        comment.setApproved(true);
        comment.setCommentDate(LocalDateTime.now());
        commentRepository.save(comment);

        // Fetch comments by commenting user
        List<Comment> comments = commentRepository.findByCommentingUser(savedUser);

        // Assert
        assertEquals(1, comments.size());
        assertEquals("Great product!", comments.get(0).getComment());
    }

    @Test
    public void testFindByCommentingUserAndApproved() {
        // Create a User
        User user = new User("Jane Doe", "456 Elm St", Role.customer);
        User savedUser = userRepository.save(user);

        // Create Comments
        Comment approvedComment = new Comment();
        approvedComment.setCommentingUser(savedUser);
        approvedComment.setComment("Approved comment");
        approvedComment.setApproved(true);
        approvedComment.setCommentDate(LocalDateTime.now());
        commentRepository.save(approvedComment);

        Comment unapprovedComment = new Comment();
        unapprovedComment.setCommentingUser(savedUser);
        unapprovedComment.setComment("Unapproved comment");
        unapprovedComment.setApproved(false);
        unapprovedComment.setCommentDate(LocalDateTime.now());
        commentRepository.save(unapprovedComment);

        // Fetch approved comments by user
        List<Comment> comments = commentRepository.findByCommentingUserAndApproved(savedUser, true);

        // Assert
        assertEquals(1, comments.size());
        assertEquals("Approved comment", comments.get(0).getComment());
    }

    @Test
    public void testFindByCommentedProduct() {
        // Create a Product
        Product product = new Product();
        product.setName("Sample Product");
        Product savedProduct = productRepository.save(product);

        // Create a Comment
        Comment comment = new Comment();
        comment.setCommentedProduct(savedProduct);
        comment.setComment("Nice product!");
        comment.setApproved(true);
        comment.setCommentDate(LocalDateTime.now());
        commentRepository.save(comment);

        // Fetch comments by product
        List<Comment> comments = commentRepository.findByCommentedProduct(savedProduct);

        // Assert
        assertEquals(1, comments.size());
        assertEquals("Nice product!", comments.get(0).getComment());
    }

    @Test
    public void testFindByCommentDateBetween() {
        // Create Comments
        Comment comment1 = new Comment();
        comment1.setComment("Comment 1");
        comment1.setCommentDate(LocalDateTime.now().minusDays(2));
        comment1.setApproved(true);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setComment("Comment 2");
        comment2.setCommentDate(LocalDateTime.now().minusDays(1));
        comment2.setApproved(true);
        commentRepository.save(comment2);

        // Fetch comments between dates
        List<Comment> comments = commentRepository.findByCommentDateBetween(
            LocalDateTime.now().minusDays(3), LocalDateTime.now());

        // Assert
        assertEquals(2, comments.size());
    }

    @Test
    public void testFindByIdAndApproved() {
        // Create a Comment
        Comment comment = new Comment();
        comment.setComment("Approved comment");
        comment.setApproved(true);
        comment.setCommentDate(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);

        // Fetch comment by ID and approved status
        Optional<Comment> fetchedComment = commentRepository.findByIdAndApproved(savedComment.getId(), true);

        // Assert
        assertTrue(fetchedComment.isPresent());
        assertEquals("Approved comment", fetchedComment.get().getComment());
    }

    @AfterEach
    void cleanUp() {
        commentRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

	@AfterAll
	static void tearDown() {
		postgresContainer.close();
	}
}