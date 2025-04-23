package com.cs308.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.List;

import com.cs308.backend.dao.Category;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Rating;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.SubmitRatingRequest;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.ProductService;
import com.cs308.backend.service.RatingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RatingControllerTest {
    private MockMvc mockMvc;

    @Mock
    private RatingService ratingService;

    @Mock
    private ProductService productService;

    @Mock
    private User mockUser;

    @Mock
    private Product mockProduct;

    @InjectMocks
    private RatingController ratingController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();
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
    public void testSubmitRating_Success() throws Exception {
        Long productId = 1L;
        int ratingValue = 5;

        SubmitRatingRequest request = new SubmitRatingRequest(productId, ratingValue);

        when(productService.findProductById(productId)).thenReturn(Optional.of(mockProduct));

        Rating existingRating = new Rating(mockProduct, mockUser, 3);
        existingRating.setId(10L);

        when(ratingService.findRatingsForProduct(mockProduct)).thenReturn(List.of(existingRating));
        when(ratingService.submitRating(existingRating)).thenReturn(Optional.of(existingRating));

        mockMvc.perform(post("/rating/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testSubmitRating_ProductNotFound() throws Exception {
        Long productId = 99L;
        int ratingValue = 5;

        SubmitRatingRequest request = new SubmitRatingRequest(productId, ratingValue);

        when(productService.findProductById(productId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/rating/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "SALES_MANAGER")
    public void testSubmitRating_ForbiddenRole() throws Exception {
        Long productId = 1L;
        int ratingValue = 5;

        SubmitRatingRequest request = new SubmitRatingRequest(productId, ratingValue);

        mockMvc.perform(post("/rating/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}