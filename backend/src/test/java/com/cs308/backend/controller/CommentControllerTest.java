package com.cs308.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.cs308.backend.dao.Category;
import com.cs308.backend.dao.Comment;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.CommentRequest;
import com.cs308.backend.dto.CommentStateRequest;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.CommentService;
import com.cs308.backend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

@AutoConfigureMockMvc
public class CommentControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @Mock
    private ProductService productService;

    @Mock
    private User mockUser;

    @Mock
    private Product mockProduct;

    @InjectMocks
    private CommentController commentController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        objectMapper = new ObjectMapper();

        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test Product");
        mockProduct.setCategory(new Category("Category 1", "Description 1"));

        // Mock authentication
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Role.customer);

        UserPrincipal userPrincipal = UserPrincipal.create(mockUser);
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testSubmitComment_Unauthenticated() throws Exception {
        CommentRequest commentRequest = new CommentRequest(mockProduct.getId(), "Test comment");

        Comment comment = new Comment(mockProduct, mockUser, "Test comment");
        comment.setId(1L);
        when(productService.findProductById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));
        when(commentService.submitComment(any(Comment.class))).thenAnswer(invocation -> {
            Comment submittedComment = invocation.getArgument(0);
            submittedComment.setId(1L); // Simulate the comment being saved with an ID
            return Optional.of(submittedComment);
        });

        mockMvc.perform(post("/comment/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequest)))
                .andExpect(status().isOk()); // Gerçek token'la testte mocklanması gerekir.
    }

    @Test
    @WithMockUser(roles = "PRODUCT_MANAGER")
    public void testApproveComment_Authorized() throws Exception {
        mockUser.setRole(Role.product_manager);
        mockProduct.setProductManager(mockUser);
        CommentStateRequest request = new CommentStateRequest(1L);

        Comment mockComment = new Comment(mockProduct, mockUser, "Test comment");
        mockComment.setId(1L);
        when(commentService.findCommentById(1L)).thenReturn(Optional.of(mockComment));

        when(commentService.approveComment(mockComment)).thenAnswer(invocation -> {
            Comment submittedComment = invocation.getArgument(0);
            submittedComment.setId(1L); // Simulate the comment being saved with an ID
            return Optional.of(submittedComment);
        });

        mockMvc.perform(post("/comment/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PRODUCT_MANAGER")
    public void testDisapproveComment_Authorized() throws Exception {
        mockUser.setRole(Role.product_manager);
        mockProduct.setProductManager(mockUser);
        CommentStateRequest request = new CommentStateRequest(1L);

        Comment mockComment = new Comment(mockProduct, mockUser, "Test comment");
        mockComment.setId(1L);
        when(commentService.findCommentById(1L)).thenReturn(Optional.of(mockComment));

        when(commentService.disapproveComment(mockComment)).thenAnswer(invocation -> {
            Comment submittedComment = invocation.getArgument(0);
            submittedComment.setId(1L); // Simulate the comment being saved with an ID
            return Optional.of(submittedComment);
        });

        mockMvc.perform(post("/comment/disapprove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
