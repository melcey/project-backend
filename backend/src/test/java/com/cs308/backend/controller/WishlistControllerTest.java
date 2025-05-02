package com.cs308.backend.controller;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.WishlistRequest;
import com.cs308.backend.dto.WishlistResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.WishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;

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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WishlistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    private ObjectMapper objectMapper;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(wishlistController).build();
        objectMapper = new ObjectMapper();

        // Mock user
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
    public void testAddToWishlist() throws Exception {
        WishlistRequest request = new WishlistRequest(1L);
        doNothing().when(wishlistService).addToWishlist(any(), any());

        mockMvc.perform(post("/wishlist/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added to wishlist."));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testGetWishlist() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Mock Product");
        mockProduct.setPrice(new BigDecimal("100.00"));
        mockProduct.setImageUrl("image.jpg");

        WishlistResponse response = new WishlistResponse(1L, "Mock Product", new BigDecimal("100.00"), "image.jpg");
        when(wishlistService.getWishlist(any())).thenReturn(List.of(response));

        mockMvc.perform(get("/wishlist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName").value("Mock Product"));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testRemoveFromWishlist() throws Exception {
        doNothing().when(wishlistService).removeFromWishlist(any(), any());

        mockMvc.perform(delete("/wishlist/remove/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product removed from wishlist."));
    }
}
