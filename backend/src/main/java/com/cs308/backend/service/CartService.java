package com.cs308.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.dao.AnonCartItem;
import com.cs308.backend.dao.Cart;
import com.cs308.backend.dao.CartItem;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.AnonCartRepository;
import com.cs308.backend.repo.CartRepository;
import com.cs308.backend.repo.ProductRepository;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AnonCartRepository anonCartRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, AnonCartRepository anonCartRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.anonCartRepository = anonCartRepository;
    }

    public Optional<Cart> anonCartToCart(Long anonCartId, User user) {
        Optional<AnonCart> anonCart = anonCartRepository.findById(anonCartId);

        if (!(anonCart.isPresent())) {
            return Optional.of(cartRepository.save(new Cart(user)));
        }

        AnonCart foundAnonCart = anonCart.get();

        Cart newCart = new Cart(user, foundAnonCart.getTotalPrice(), null);

        List<CartItem> cartItems = new ArrayList<>();

        for (AnonCartItem anonCartItem: foundAnonCart.getItems()) {
            cartItems.add(new CartItem(newCart, anonCartItem.getProduct(), anonCartItem.getQuantity(), anonCartItem.getPriceAtAddition()));
        }

        newCart.setItems(cartItems);

        try {
            Cart createdCart = cartRepository.save(newCart);
            return Optional.of(createdCart);
        }
        catch (Exception e) {
            return Optional.of(cartRepository.save(new Cart(user)));
        }
    }

    public Optional<Cart> createEmptyCart(User user) {
        Cart cart = new Cart(user);

        try {
            Cart createdCart = cartRepository.save(cart);
            return Optional.of(createdCart);
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Cart> getCartOfUser(User user) {
        return cartRepository.findByUser(user);
    }

    public Optional<Cart> addItemToCart(User user, Long productId, int quantity) {
        Optional<Cart> cart = cartRepository.findByUser(user);

        if (!(cart.isPresent())) {
            return Optional.empty();
        }

        Cart foundCart = cart.get();

        Optional<Product> product = productRepository.findById(productId);

        if (!(product.isPresent())) {
            return cart;
        }

        Product foundProduct = product.get();

        Cart oldCart = foundCart.clone();

        if (foundProduct.getQuantityInStock() == 0) {
            return Optional.of(oldCart);
        }

        CartItem cartItem = cart.get().getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(new CartItem());

        cartItem.setCart(foundCart);
        cartItem.setProduct(foundProduct);
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setPriceAtAddition(foundProduct.getPrice());

        foundCart.getItems().add(cartItem);
        foundCart.setTotalPrice(foundCart.getItems().stream()
            .map(item -> item.getPriceAtAddition().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add));
        foundCart.setUpdatedAt(LocalDateTime.now());

        try {
            Cart updatedCart = cartRepository.save(foundCart);
            return Optional.of(updatedCart);
        }
        catch (Exception e) {
            return Optional.of(oldCart);
        }
    }
}