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
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.dao.AnonCartItem;
import com.cs308.backend.dao.Cart;
import com.cs308.backend.dao.CartItem;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.AnonCartRepository;
import com.cs308.backend.repo.CartRepository;
import com.cs308.backend.repo.ProductRepository;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AnonCartRepository anonCartRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAnonCartToCart() {
        // Mock data
        User user = new User("John Doe", "123 Main St", Role.customer);
        AnonCart anonCart = new AnonCart();
        anonCart.setId(1L);
        anonCart.setTotalPrice(new BigDecimal("100.00"));

        AnonCartItem anonCartItem = new AnonCartItem();
        anonCartItem.setProduct(new Product());
        anonCartItem.setQuantity(2);
        anonCartItem.setPriceAtAddition(new BigDecimal("50.00"));
        anonCart.getItems().add(anonCartItem);

        when(anonCartRepository.findById(1L)).thenReturn(Optional.of(anonCart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        Optional<Cart> cart = cartService.anonCartToCart(1L, user);

        // Assert
        assertTrue(cart.isPresent());
        assertEquals(1, cart.get().getItems().size());
        assertEquals(new BigDecimal("100.00"), cart.get().getTotalPrice());

        verify(anonCartRepository, times(1)).findById(1L);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testCreateEmptyCart() {
        // Mock data
        User user = new User("Jane Doe", "456 Elm St", Role.customer);
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        Optional<Cart> cart = cartService.createEmptyCart(user);

        // Assert
        assertTrue(cart.isPresent());
        assertEquals(user, cart.get().getUser());

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testGetCartOfUser() {
        // Mock data
        User user = new User("John Doe", "123 Main St", Role.customer);
        Cart cart = new Cart(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        // Call the service method
        Optional<Cart> fetchedCart = cartService.getCartOfUser(user);

        // Assert
        assertTrue(fetchedCart.isPresent());
        assertEquals(user, fetchedCart.get().getUser());

        verify(cartRepository, times(1)).findByUser(user);
    }

    @Test
    public void testAddItemToCart() {
        // Mock data
        User user = new User("John Doe", "123 Main St", Role.customer);
        Cart cart = new Cart(user);
        cart.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal("50.00"));
        product.setQuantityInStock(10);

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the service method
        Optional<Cart> updatedCart = cartService.addItemToCart(user, 1L, 2);

        // Assert
        assertTrue(updatedCart.isPresent());
        assertEquals(1, updatedCart.get().getItems().size());
        CartItem item = updatedCart.get().getItems().get(0);
        assertEquals(1L, item.getProduct().getId());
        assertEquals(2, item.getQuantity());
        assertEquals(new BigDecimal("100.00"), updatedCart.get().getTotalPrice());

        verify(cartRepository, times(1)).findByUser(user);
        verify(productRepository, times(1)).findById(1L);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testAddItemToCart_ProductNotFound() {
        // Mock data
        User user = new User("John Doe", "123 Main St", Role.customer);
        Cart cart = new Cart(user);
        cart.setId(1L);

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method
        Optional<Cart> updatedCart = cartService.addItemToCart(user, 1L, 2);

        // Assert
        assertTrue(updatedCart.isPresent());
        assertEquals(0, updatedCart.get().getItems().size());

        verify(cartRepository, times(1)).findByUser(user);
        verify(productRepository, times(1)).findById(1L);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    public void testAddItemToCart_CartNotFound() {
        // Mock data
        User user = new User("John Doe", "123 Main St", Role.customer);
        Cart newCart = new Cart(user);

        when(cartRepository.findByUser(user)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(newCart);

        // Call the service method
        Optional<Cart> updatedCart = cartService.addItemToCart(user, 1L, 2);

        // Assert
        assertTrue(updatedCart.isPresent());
        assertEquals(user, updatedCart.get().getUser());
        assertEquals(0, updatedCart.get().getItems().size());

        verify(cartRepository, times(1)).findByUser(user);
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(productRepository, never()).findById(anyLong());
    }
}