package com.cs308.backend.service;

import com.cs308.backend.dao.Cart;
import com.cs308.backend.dao.CartItem;
import com.cs308.backend.dao.Product;
import com.cs308.backend.repo.CartRepository;
import com.cs308.backend.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, ProductService productService, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    public Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(userRepository.findById(userId).get());
            return cartRepository.save(cart);
        });
    }

    public Cart addItemToCart(Long userId, Long productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        Product product = productService.findProductById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(new CartItem());

        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setPriceAtAddition(product.getPrice());

        cart.getItems().add(cartItem);
        cart.setTotalPrice(cart.getItems().stream()
            .map(item -> item.getPriceAtAddition().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        return cartRepository.save(cart);
    }
}