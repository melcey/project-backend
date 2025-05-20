package com.cs308.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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

import com.cs308.backend.dao.Cart;
import com.cs308.backend.dao.CartItem;
import com.cs308.backend.dao.Category;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.CartItemRequest;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.AnonCartService;
import com.cs308.backend.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
public class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CartService cartService;

    @Mock
    private AnonCartService anonCartService;

    @InjectMocks
    private CartController cartController;

    private ObjectMapper objectMapper;
    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        objectMapper = new ObjectMapper();

        testUser = new User();
        testUser.setId(1L);
        testUser.setRole(Role.customer);

        UserPrincipal userPrincipal = UserPrincipal.create(testUser);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testGetCart_Success() throws Exception {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(100));
        Category category = new Category("cat", "desc");
        product.setCategory(category);

        CartItem item = new CartItem();
        item.setId(1L);
        item.setProduct(product);
        item.setQuantity(1);
        item.setPriceAtAddition(BigDecimal.valueOf(100));

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(List.of(item));
        cart.setTotalPrice(BigDecimal.valueOf(100));

        when(cartService.getCartOfUser(any(User.class))).thenReturn(Optional.of(cart));

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(100.0));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testAddItemToCart_Success() throws Exception {
        Long productId = 1L;
        int quantity = 2;

        Product product = new Product();
        product.setId(productId);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(50));
        product.setCategory(new Category("cat", "desc"));

        CartItem item = new CartItem();
        item.setId(1L);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPriceAtAddition(BigDecimal.valueOf(50));

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(List.of(item));
        cart.setTotalPrice(BigDecimal.valueOf(100));

        when(cartService.addItemToCart(any(User.class), eq(productId), eq(quantity)))
                .thenReturn(Optional.of(cart));

        CartItemRequest request = new CartItemRequest(productId, quantity);

        mockMvc.perform(put("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(100.0));
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testDeleteItemFromCart_Success() throws Exception {
        Long productId = 1L;
        int quantity = 1;

        Product product = new Product();
        product.setId(productId);
        product.setName("Product");
        product.setPrice(BigDecimal.valueOf(100));
        product.setCategory(new Category("cat", "desc"));

        CartItem item = new CartItem();
        item.setId(1L);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPriceAtAddition(BigDecimal.valueOf(100));

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(List.of(item));
        cart.setTotalPrice(BigDecimal.valueOf(100));

        when(cartService.deleteItemFromCart(any(User.class), eq(productId), eq(quantity)))
                .thenReturn(Optional.of(cart));

        CartItemRequest request = new CartItemRequest(productId, quantity);

        mockMvc.perform(put("/cart/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(100.0));
    }
    @Test
public void testGetCart_Unauthorized() throws Exception {
    // Auth'ı temizleyerek giriş yapılmamış gibi davranılır
    SecurityContextHolder.clearContext();

    mockMvc.perform(get("/cart"))
        .andExpect(status().isUnauthorized());
}

}
