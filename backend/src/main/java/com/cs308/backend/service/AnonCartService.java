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
            AnonCart newAnonCart = new AnonCart();

            Optional<Product> product = productService.findProductById(productId);

            if (!(product.isPresent())) {
                AnonCart updatedAnonCart = anonCartRepository.save(newAnonCart);
                return Optional.of(updatedAnonCart);
            }

            Product foundProduct = product.get();

            AnonCart oldAnonCart = newAnonCart.clone();

            if (foundProduct.getQuantityInStock() == 0) {
                return Optional.of(oldAnonCart);
            }

            AnonCartItem anonCartItem = new AnonCartItem();

            anonCartItem.setCart(newAnonCart);
            anonCartItem.setProduct(foundProduct);
            anonCartItem.setQuantity(anonCartItem.getQuantity() + quantity);
            anonCartItem.setPriceAtAddition(foundProduct.getPrice());
            
            newAnonCart.getItems().add(anonCartItem);
            newAnonCart.setTotalPrice(foundProduct.getPrice().multiply(BigDecimal.valueOf(quantity)));
            newAnonCart.setUpdatedAt(LocalDateTime.now());

            try {
                AnonCart updatedAnonCart = anonCartRepository.save(newAnonCart);
                return Optional.of(updatedAnonCart);
            }
            catch (Exception e) {
                return Optional.of(oldAnonCart);
            }
        }
        else {
            AnonCart foundAnonCart = anonCart.get();

            if (foundAnonCart.getItems() == null) {
                foundAnonCart.setItems(new ArrayList<>());
            }

            Optional<Product> product = productService.findProductById(productId);

            if (!(product.isPresent())) {
                return anonCart;
            }

            Product foundProduct = product.get();

            AnonCart oldAnonCart = foundAnonCart.clone();

            if (foundProduct.getQuantityInStock() < quantity) {
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
            
            Set<AnonCartItem> setOfAnonCartItems = new LinkedHashSet<>(foundAnonCart.getItems());
            setOfAnonCartItems.add(anonCartItem);

            foundAnonCart.setItems(new ArrayList<>(setOfAnonCartItems));
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

    public Optional<AnonCart> deleteItemFromAnonCart(Long anonCartId, Long productId, int quantity) {
        Optional<AnonCart> anonCart = anonCartRepository.findById(anonCartId);

        if (!(anonCart.isPresent())) {
            AnonCart newAnonCart = new AnonCart();
            newAnonCart = anonCartRepository.save(newAnonCart);

            return Optional.of(newAnonCart);
        }

        AnonCart foundAnonCart = anonCart.get();

        if (foundAnonCart.getItems() == null) {
            foundAnonCart.setItems(new ArrayList<>());
        }

        Optional<Product> product = productService.findProductById(productId);

        if (!(product.isPresent())) {
            return anonCart;
        }

        Product foundProduct = product.get();

        AnonCart oldAnonCart = foundAnonCart.clone();

        AnonCartItem anonCartItem = anonCart.get().getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(new AnonCartItem());

        if (quantity == anonCartItem.getQuantity()) {
            Set<AnonCartItem> setOfAnonCartItems = new LinkedHashSet<>(foundAnonCart.getItems());
            setOfAnonCartItems.remove(anonCartItem);

            foundAnonCart.setItems(new ArrayList<>(setOfAnonCartItems));

            try {
                AnonCart updatedAnonCart = anonCartRepository.save(foundAnonCart);
                return Optional.of(updatedAnonCart);
            }
            catch (Exception e) {
                return Optional.of(oldAnonCart);
            }
        }
        else if (quantity < anonCartItem.getQuantity()) {
            anonCartItem.setCart(foundAnonCart);
            anonCartItem.setProduct(foundProduct);
            anonCartItem.setQuantity(anonCartItem.getQuantity() - quantity);
            anonCartItem.setPriceAtAddition(foundProduct.getPrice());

            Set<AnonCartItem> setOfAnonCartItems = new LinkedHashSet<>(foundAnonCart.getItems());
            setOfAnonCartItems.add(anonCartItem);

            foundAnonCart.setItems(new ArrayList<>(setOfAnonCartItems));
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
        else {
            return Optional.of(oldAnonCart);
        }
    }
}