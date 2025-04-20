package com.cs308.backend.service;

import java.util.Optional;

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.dao.AnonCartItem;
import com.cs308.backend.dao.Product;
import com.cs308.backend.repo.AnonCartRepository;
import com.cs308.backend.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AnonCartService {
    private final AnonCartRepository anonCartRepository;
    private final ProductService productService;

    public AnonCartService(AnonCartRepository anonCartRepository, ProductService productService) {
        this.anonCartRepository = anonCartRepository;
        this.productService = productService;
    }

    public Optional<AnonCart> createAnonCart() {
        AnonCart anonCart = new AnonCart();

        try {
            AnonCart createdAnonCart = anonCartRepository.save(anonCart);
            return Optional.of(createdAnonCart);
            // AnonCart's ID should be kept
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<AnonCart> getAnonCart(Long anonCartId) {
        return anonCartRepository.findById(anonCartId);
    }

    public AnonCart addItemToAnonCart(Long userId, Long productId, int quantity) {
        AnonCart anonCart = getOrCreateAnonCart(userId);
        Product product = productService.findProductById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        AnonCartItem anonCartItem = anonCart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(new CartItem());

        anonCartItem.setCart(cart);
        anonCartItem.setProduct(product);
        anonCartItem.setQuantity(anonCartItem.getQuantity() + quantity);
        anonCartItem.setPriceAtAddition(product.getPrice());

        anonCart.getItems().add(anonCartItem);
        anonCart.setTotalPrice(anonCart.getItems().stream()
            .map(item -> item.getPriceAtAddition().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        return anonCartRepository.save(anonCart);
    }
}