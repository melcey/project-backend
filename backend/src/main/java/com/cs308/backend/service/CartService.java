package com.cs308.backend.service;

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.dao.AnonCartItem;
import com.cs308.backend.dao.Cart;
import com.cs308.backend.dao.CartItem;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.AnonCartRepository;
import com.cs308.backend.repo.CartRepository;
import com.cs308.backend.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final AnonCartRepository anonCartRepository;

    public CartService(CartRepository cartRepository, ProductService productService, UserRepository userRepository, AnonCartRepository anonCartRepository) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.userRepository = userRepository;
        this.anonCartRepository = anonCartRepository;
    }

    public Optional<Cart> anonCartToCart(Long anonCartId, User user) {
        Optional<AnonCart> anonCart = anonCartRepository.findById(anonCartId);

        if (!(anonCart.isPresent())) {
            return Optional.empty();
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
            return Optional.empty();
        }
    }

    public Optional<Cart> createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);

        try {
            Cart createdCart = cartRepository.save(cart);
            return Optional.of(createdCart);
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Cart> getCartOfUser(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public Optional<Cart> addItemToCart(Long userId, Long productId, int quantity) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);

        if (!(cart.isPresent())) {
            return Optional.empty();
        }

        Cart foundCart = cart.get();

        Optional<Product> product = productService.findProductById(productId);

        if (!(product.isPresent())) {
            return cart;
        }

        Product foundProduct = product.get();

        Cart oldCart = foundCart.clone();


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

        try {
            Cart updatedCart = cartRepository.save(foundCart);
            return Optional.of(updatedCart);
        }
        catch (Exception e) {
            return Optional.of(oldCart);
        }
    }
}