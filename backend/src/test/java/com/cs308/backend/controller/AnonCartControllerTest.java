package com.cs308.backend.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.dao.AnonCartItem;
import com.cs308.backend.dao.Category;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dto.AnonCartItemRequest;
import com.cs308.backend.service.AnonCartService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
public class AnonCartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AnonCartService anonCartService;

    @InjectMocks
    private AnonCartController anonCartController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(anonCartController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeleteItemFromAnonCart_success() throws Exception {
        Long cartId = 1L;
        Long productId = 10L;

        // Dummy product
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.0));

        Category category = new Category();
        category.setId(5L);
        category.setName("Electronics");
        category.setDescription("Devices");
        product.setCategory(category);

        // Dummy item
        AnonCartItem item = new AnonCartItem();
        item.setId(101L);
        item.setProduct(product);
        item.setQuantity(2);
        item.setPriceAtAddition(BigDecimal.valueOf(100.0));

        // Dummy cart
        AnonCart cart = new AnonCart();
        cart.setId(cartId);
        cart.setItems(List.of(item));
        cart.setTotalPrice(BigDecimal.valueOf(200.0));

        // Mock service
        when(anonCartService.deleteItemFromAnonCart(eq(cartId), eq(productId), eq(2)))
                .thenReturn(Optional.of(cart));

        // Request body
        AnonCartItemRequest request = new AnonCartItemRequest(productId, 2);

        // Perform PUT request
        mockMvc.perform(put("/anoncart/{id}/delete", cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cartId))
                .andExpect(jsonPath("$.totalPrice").value(200.0))
                .andExpect(jsonPath("$.items[0].product.id").value(productId));
    }
    @Test
public void testCreateAnonCart_success() throws Exception {
    AnonCart newCart = new AnonCart();
    newCart.setId(1L);
    newCart.setTotalPrice(BigDecimal.ZERO);
    newCart.setItems(List.of());

    when(anonCartService.createAnonCart()).thenReturn(Optional.of(newCart));

    mockMvc.perform(post("/anoncart"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.totalPrice").value(0.0))
            .andExpect(jsonPath("$.items").isEmpty());
}
@Test
public void testGetAnonCart_success() throws Exception {
    Long cartId = 1L;

    Product product = new Product();
    product.setId(10L);
    product.setName("Test Product");
    product.setPrice(BigDecimal.valueOf(100.0));
    Category category = new Category();
    category.setId(5L);
    category.setName("Electronics");
    category.setDescription("Devices");
    product.setCategory(category);

    AnonCartItem item = new AnonCartItem();
    item.setId(101L);
    item.setProduct(product);
    item.setQuantity(1);
    item.setPriceAtAddition(BigDecimal.valueOf(100.0));

    AnonCart cart = new AnonCart();
    cart.setId(cartId);
    cart.setItems(List.of(item));
    cart.setTotalPrice(BigDecimal.valueOf(100.0));

    when(anonCartService.getAnonCart(cartId)).thenReturn(Optional.of(cart));

    mockMvc.perform(get("/anoncart/{id}", cartId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(cartId))
            .andExpect(jsonPath("$.totalPrice").value(100.0))
            .andExpect(jsonPath("$.items[0].product.id").value(10L));
}
@Test
public void testAddItemToAnonCart_success() throws Exception {
    Long cartId = 1L;
    Long productId = 10L;

    Product product = new Product();
    product.setId(productId);
    product.setName("Test Product");
    product.setPrice(BigDecimal.valueOf(50.0));
    Category category = new Category();
    category.setId(2L);
    category.setName("Toys");
    category.setDescription("For kids");
    product.setCategory(category);

    AnonCartItem item = new AnonCartItem();
    item.setId(102L);
    item.setProduct(product);
    item.setQuantity(2);
    item.setPriceAtAddition(BigDecimal.valueOf(50.0));

    AnonCart cart = new AnonCart();
    cart.setId(cartId);
    cart.setItems(List.of(item));
    cart.setTotalPrice(BigDecimal.valueOf(100.0));

    when(anonCartService.addItemToAnonCart(eq(cartId), eq(productId), eq(2)))
            .thenReturn(Optional.of(cart));

    AnonCartItemRequest request = new AnonCartItemRequest(productId, 2);

    mockMvc.perform(put("/anoncart/{id}/add", cartId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(cartId))
            .andExpect(jsonPath("$.totalPrice").value(100.0))
            .andExpect(jsonPath("$.items[0].product.id").value(productId));
}
@Test
public void testDeleteItemFromAnonCart_notFound() throws Exception {
    Long cartId = 999L;
    Long productId = 99L;

    when(anonCartService.deleteItemFromAnonCart(eq(cartId), eq(productId), eq(1)))
            .thenReturn(Optional.empty());

    AnonCartItemRequest request = new AnonCartItemRequest(productId, 1);

    mockMvc.perform(put("/anoncart/{id}/delete", cartId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
}

}
