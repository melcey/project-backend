package com.cs308.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dao.Wishlist;
import com.cs308.backend.dto.WishlistItemRequest;
import com.cs308.backend.dto.WishlistResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.WishlistService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
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
        WishlistItemRequest request = new WishlistItemRequest(1L);

        when(wishlistService.addToWishlist(any(), eq(1L))).thenReturn(Optional.of(new Wishlist()));

        mockMvc.perform(put("/wishlist/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testGetWishlist() throws Exception {
        when(wishlistService.getWishlist(any())).thenReturn(Optional.of(new Wishlist()));

        mockMvc.perform(get("/wishlist"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testRemoveFromWishlist() throws Exception {
        WishlistItemRequest request = new WishlistItemRequest(1L);
        when(wishlistService.removeFromWishlist(any(), eq(1L))).thenReturn(Optional.of(new Wishlist()));

        mockMvc.perform(delete("/wishlist/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
