package com.cs308.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.List;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Rating;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.ProductRepository;
import com.cs308.backend.repo.RatingRepository;
import com.cs308.backend.security.UserPrincipal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RatingController.class)
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingRepository ratingRepository;

    @MockBean
    private ProductRepository productRepository;

    @Mock
    private UserPrincipal mockUserPrincipal;

    @Mock
    private User mockUser;

    @Mock
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testSubmitRating_Success() throws Exception {
        Long productId = 1L;
        int ratingValue = 5;

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));
        when(mockUser.getRole()).thenReturn(Role.customer);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUserPrincipal.getUser()).thenReturn(mockUser);

        Rating existingRating = new Rating(mockProduct, mockUser, 3);
        existingRating.setId(10L);

        // Stream içinde bu rating varmış gibi simüle
        when(ratingRepository.findAll()).thenReturn(List.of(existingRating));

        mockMvc.perform(post("/ratings/submit")
                .param("productId", productId.toString())
                .param("ratingValue", String.valueOf(ratingValue)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testSubmitRating_ProductNotFound() throws Exception {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/ratings/submit")
                .param("productId", "99")
                .param("ratingValue", "5"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "SALES_MANAGER")
    public void testSubmitRating_ForbiddenRole() throws Exception {
        mockMvc.perform(post("/ratings/submit")
                .param("productId", "1")
                .param("ratingValue", "5"))
                .andExpect(status().isForbidden());
    }
}
