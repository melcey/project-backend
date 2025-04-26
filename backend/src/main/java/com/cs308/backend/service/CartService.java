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
        Optional<Cart> cartOpt = cartRepository.findByUser(user);

        Cart cart;
        if (cartOpt.isPresent()) {
            cart = cartOpt.get();
        } else {
            // Eğer kullanıcının cart'ı yoksa yeni bir cart oluştur
            cart = new Cart();
            cart.setUser(user);
            cart.setTotalPrice(BigDecimal.ZERO);
            cart.setCreatedAt(LocalDateTime.now());
            cart.setUpdatedAt(LocalDateTime.now());
            cart.setItems(new ArrayList<>());  // boş liste başlat
            cart = cartRepository.save(cart); // kaydet
        }

        // Ürünü bul
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            return Optional.of(cart); // Ürün bulunamadıysa cart'ı yine de dön
        }

        Product product = productOpt.get();

        if (product.getQuantityInStock() == 0) {
            return Optional.of(cart); // Stok yoksa cart'ı yine de dön
        }

        // Cart'ta bu ürün zaten var mı?
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            // Ürün yoksa yeni cartItem oluştur
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(0);
            cartItem.setCreatedAt(LocalDateTime.now()); // BURASI EKLENDİ
            cart.getItems().add(cartItem);
        }

        // Miktarı ve fiyatı güncelle
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setPriceAtAddition(product.getPrice());

        // Total price güncelle
        cart.setTotalPrice(cart.getItems().stream()
                .map(item -> item.getPriceAtAddition().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        cart.setUpdatedAt(LocalDateTime.now());

        Cart updatedCart = cartRepository.save(cart);
        return Optional.of(updatedCart);
    }
}