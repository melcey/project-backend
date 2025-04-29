package com.cs308.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
        Optional<Cart> retrievedCart = cartRepository.findByUser(user);

        if (!(retrievedCart.isPresent())) {
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
                e.printStackTrace();
                return Optional.of(cartRepository.save(new Cart(user)));
            }
        }
        else {
            Optional<AnonCart> anonCart = anonCartRepository.findById(anonCartId);

            if (!(anonCart.isPresent())) {
                return retrievedCart;
            }

            Cart existingCart = retrievedCart.get();

            AnonCart foundAnonCart = anonCart.get();

            existingCart.getItems().clear();
            existingCart.setTotalPrice(foundAnonCart.getTotalPrice());

            List<CartItem> cartItems = new ArrayList<>();

            for (AnonCartItem anonCartItem: foundAnonCart.getItems()) {
                cartItems.add(new CartItem(existingCart, anonCartItem.getProduct(), anonCartItem.getQuantity(), anonCartItem.getPriceAtAddition()));
            }

            existingCart.getItems().addAll(cartItems);

            try {
                Cart createdCart = cartRepository.save(existingCart);
                return Optional.of(createdCart);
            }
            catch (Exception e) {
                e.printStackTrace();
                return retrievedCart;
            }
        }
    }

    public Optional<Cart> createEmptyCart(User user) {
        Cart cart = new Cart(user);

        try {
            Cart createdCart = cartRepository.save(cart);
            return Optional.of(createdCart);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Cart> getCartOfUser(User user) {
        return cartRepository.findByUser(user);
    }

    public Optional<Cart> addItemToCart(User user, Long productId, int quantity) {
        Optional<Cart> cart = cartRepository.findByUser(user);

        if (!(cart.isPresent())) {
            Cart newCart = new Cart(user);

            Optional<Product> product = productRepository.findById(productId);

            if (!(product.isPresent())) {
                Cart updatedCart = cartRepository.save(newCart);
                return Optional.of(updatedCart);
            }

            Product foundProduct = product.get();

            if (foundProduct.getQuantityInStock() < quantity) {
                newCart = cartRepository.save(newCart);
                return Optional.of(newCart);
            }

            CartItem cartItem = new CartItem();

            cartItem.setCart(newCart);
            cartItem.setProduct(foundProduct);
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setPriceAtAddition(foundProduct.getPrice());
            
            newCart.getItems().add(cartItem);
            newCart.setTotalPrice(foundProduct.getPrice().multiply(BigDecimal.valueOf(quantity)));
            newCart.setUpdatedAt(LocalDateTime.now());

            try {
                Cart updatedCart = cartRepository.save(newCart);
                return Optional.of(updatedCart);
            }
            catch (Exception e) {
                e.printStackTrace();
                newCart = cartRepository.save(newCart);
                return Optional.of(newCart);
            }
        }
        else {
            Cart foundCart = cart.get();

            Optional<Product> product = productRepository.findById(productId);

            if (!(product.isPresent())) {
                foundCart = cartRepository.save(foundCart);
                return Optional.of(foundCart);
            }

            Product foundProduct = product.get();

            Cart oldCart = foundCart.clone();

            if (foundProduct.getQuantityInStock() < quantity) {
                oldCart = cartRepository.save(oldCart);
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
            
            Set<CartItem> setOfCartItems = new LinkedHashSet<>(foundCart.getItems());
            setOfCartItems.add(cartItem);
            
            foundCart.getItems().clear();
            foundCart.getItems().addAll(setOfCartItems);
            foundCart.setTotalPrice(foundCart.getItems().stream()
                .map(item -> item.getPriceAtAddition().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
            foundCart.setUpdatedAt(LocalDateTime.now());

            try {
                Cart updatedCart = cartRepository.save(foundCart);
                return Optional.of(updatedCart);
            }
            catch (Exception e) {
                e.printStackTrace();
                oldCart = cartRepository.save(oldCart);
                return Optional.of(oldCart);
            }
        }
    }

    public Optional<Cart> deleteItemFromCart(User user, Long productId, int quantity) {
        Optional<Cart> cart = cartRepository.findByUser(user);

        if (!(cart.isPresent())) {
            Cart newCart = new Cart(user);
            newCart = cartRepository.save(newCart);

            return Optional.of(newCart);
        }

        Cart foundCart = cart.get();

        if (foundCart.getItems() == null) {
            foundCart.setItems(new ArrayList<>());
        }

        Optional<Product> product = productRepository.findById(productId);

        if (!(product.isPresent())) {
            foundCart = cartRepository.save(foundCart);
            return Optional.of(foundCart);
        }

        Product foundProduct = product.get();

        Cart oldCart = foundCart.clone();

        CartItem cartItem = cart.get().getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(new CartItem());

        if (quantity == cartItem.getQuantity()) {
            Set<CartItem> setOfCartItems = new LinkedHashSet<>(foundCart.getItems());
            setOfCartItems.remove(cartItem);
            
            foundCart.getItems().clear();
            foundCart.getItems().addAll(setOfCartItems);
            foundCart.setTotalPrice(foundCart.getTotalPrice().subtract(cartItem.getPriceAtAddition().multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
            foundCart.setUpdatedAt(LocalDateTime.now());

            try {
                Cart updatedCart = cartRepository.save(foundCart);
                return Optional.of(updatedCart);
            }
            catch (Exception e) {
                e.printStackTrace();
                oldCart = cartRepository.save(oldCart);
                return Optional.of(oldCart);
            }
        }
        else if (quantity < cartItem.getQuantity()) {
            cartItem.setCart(foundCart);
            cartItem.setProduct(foundProduct);
            cartItem.setQuantity(cartItem.getQuantity() - quantity);
            cartItem.setPriceAtAddition(foundProduct.getPrice());

            Set<CartItem> setOfCartItems = new LinkedHashSet<>(foundCart.getItems());
            setOfCartItems.add(cartItem);
            
            foundCart.getItems().clear();
            foundCart.getItems().addAll(setOfCartItems);
            foundCart.setTotalPrice(foundCart.getItems().stream()
                .map(item -> item.getPriceAtAddition().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
            foundCart.setUpdatedAt(LocalDateTime.now());

            try {
                Cart updatedCart = cartRepository.save(foundCart);
                return Optional.of(updatedCart);
            }
            catch (Exception e) {
                e.printStackTrace();
                oldCart = cartRepository.save(oldCart);
                return Optional.of(oldCart);
            }
        }
        else {
            oldCart = cartRepository.save(oldCart);
            return Optional.of(oldCart);
        }
    }

    public Optional<Cart> clearCart(User user) {
        Optional<Cart> cart = cartRepository.findByUser(user);

        if (!(cart.isPresent())) {
            Cart newCart = new Cart(user);
            newCart = cartRepository.save(newCart);

            return Optional.of(newCart);
        }

        Cart foundCart = cart.get();

        if (foundCart.getItems() == null) {
            foundCart.setItems(new ArrayList<>());
        }

        Cart oldCart = foundCart.clone();

        foundCart.getItems().clear();

        try {
            Cart updatedCart = cartRepository.save(foundCart);
            return Optional.of(updatedCart);
        }
        catch (Exception e) {
            e.printStackTrace();
            oldCart = cartRepository.save(oldCart);
            return Optional.of(oldCart);
        }
    }
}