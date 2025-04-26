package com.cs308.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.dao.AnonCartItem;
import com.cs308.backend.dao.Product;
import com.cs308.backend.repo.AnonCartRepository;

public class AnonCartServiceTest {

    @Mock
    private AnonCartRepository anonCartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private AnonCartService anonCartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAnonCart() {
        // Mock the repository save method
        AnonCart mockCart = new AnonCart();
        when(anonCartRepository.save(any(AnonCart.class))).thenReturn(mockCart);

        // Call the service method
        Optional<AnonCart> createdCart = anonCartService.createAnonCart();

        // Assert the result
        assertTrue(createdCart.isPresent());
        verify(anonCartRepository, times(1)).save(any(AnonCart.class));
    }

    @Test
    public void testGetAnonCart() {
        // Mock the repository findById method
        AnonCart mockCart = new AnonCart();
        when(anonCartRepository.findById(1L)).thenReturn(Optional.of(mockCart));

        // Call the service method
        Optional<AnonCart> fetchedCart = anonCartService.getAnonCart(1L);

        // Assert the result
        assertTrue(fetchedCart.isPresent());
        verify(anonCartRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddItemToAnonCart() {
        // Mock the repository and product service
        AnonCart mockCart = new AnonCart();
        mockCart.setId(1L);
        mockCart.setItems(new ArrayList<>()); // Ensure items list is initialized

        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setPrice(new BigDecimal("50.00"));
        mockProduct.setQuantityInStock(10);

        when(anonCartRepository.findById(1L)).thenReturn(Optional.of(mockCart));
        when(productService.findProductById(1L)).thenReturn(Optional.of(mockProduct));
        when(anonCartRepository.save(any(AnonCart.class))).thenReturn(mockCart);

        // Call the service method with valid inputs
        Optional<AnonCart> updatedCart = anonCartService.addItemToAnonCart(1L, 1L, 2);

        // Assert the result
        assertTrue(updatedCart.isPresent());
        assertEquals(1, updatedCart.get().getItems().size());

        AnonCartItem item = updatedCart.get().getItems().get(0);
        assertEquals(1L, item.getProduct().getId());
        assertEquals(2, item.getQuantity());
        assertEquals(new BigDecimal("50.00"), item.getPriceAtAddition());
        assertEquals(new BigDecimal("100.00"), updatedCart.get().getTotalPrice());

        // Verify that the item is correctly linked to the cart
        assertEquals(mockCart, item.getCart());

        // Verify interactions with mocks
        verify(anonCartRepository, times(1)).findById(1L);
        verify(productService, times(1)).findProductById(1L);
        verify(anonCartRepository, times(1)).save(any(AnonCart.class));
    }

    @Test
    public void testAddItemToAnonCart_ProductNotFound() {
        // Mock the repository and product service
        AnonCart mockCart = new AnonCart();
        mockCart.setId(1L);

        when(anonCartRepository.findById(1L)).thenReturn(Optional.of(mockCart));
        when(productService.findProductById(1L)).thenReturn(Optional.empty());

        // Call the service method
        Optional<AnonCart> updatedCart = anonCartService.addItemToAnonCart(1L, 1L, 2);

        // Assert the result
        assertTrue(updatedCart.isPresent());
        assertEquals(0, updatedCart.get().getItems().size());

        verify(anonCartRepository, times(1)).findById(1L);
        verify(productService, times(1)).findProductById(1L);
        verify(anonCartRepository, never()).save(any(AnonCart.class));
    }

    @Test
    public void testAddItemToAnonCart_CartNotFound() {
        // Mock the repository
        AnonCart newAnonCart = new AnonCart();
        when(anonCartRepository.findById(1L)).thenReturn(Optional.empty());
        when(anonCartRepository.save(any(AnonCart.class))).thenReturn(newAnonCart);

        // Call the service method
        Optional<AnonCart> updatedCart = anonCartService.addItemToAnonCart(1L, 1L, 2);

        // Assert the result
        assertTrue(updatedCart.isPresent());
        assertEquals(0, updatedCart.get().getItems().size());

        verify(anonCartRepository, times(1)).findById(1L);
        verify(anonCartRepository, times(1)).save(any(AnonCart.class));
        verify(productService, never()).findProductById(anyLong());
    }
}