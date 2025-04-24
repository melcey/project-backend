package com.cs308.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.dao.AnonCartItem;
import com.cs308.backend.dao.Product;
import com.cs308.backend.repo.AnonCartRepository;

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
            // AnonCart's ID should be kept in the frontend
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<AnonCart> getAnonCart(Long anonCartId) {
        return anonCartRepository.findById(anonCartId);
    }

    public Optional<AnonCart> addItemToAnonCart(Long anonCartId, Long productId, int quantity) {
        Optional<AnonCart> anonCart = anonCartRepository.findById(anonCartId);

        if (!(anonCart.isPresent())) {
            return Optional.empty();
        }

        AnonCart foundAnonCart = anonCart.get();

        Optional<Product> product = productService.findProductById(productId);

        if (!(product.isPresent())) {
            return anonCart;
        }

        Product foundProduct = product.get();

        AnonCart oldAnonCart = foundAnonCart.clone();

        if (foundProduct.getQuantityInStock() == 0) {
            return Optional.of(oldAnonCart);
        }

        AnonCartItem anonCartItem = anonCart.get().getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(new AnonCartItem());

        anonCartItem.setCart(foundAnonCart);
        anonCartItem.setProduct(foundProduct);
        anonCartItem.setQuantity(anonCartItem.getQuantity() + quantity);
        anonCartItem.setPriceAtAddition(foundProduct.getPrice());

        foundAnonCart.getItems().add(anonCartItem);
        foundAnonCart.setTotalPrice(foundAnonCart.getItems().stream()
            .map(item -> item.getPriceAtAddition().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add));
        foundAnonCart.setUpdatedAt(LocalDateTime.now());

        try {
            AnonCart updatedAnonCart = anonCartRepository.save(foundAnonCart);
            return Optional.of(updatedAnonCart);
        }
        catch (Exception e) {
            return Optional.of(oldAnonCart);
        }
    }
}