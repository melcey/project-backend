package com.cs308.backend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.dao.AnonCartItem;
import com.cs308.backend.dao.Product;
import com.cs308.backend.repo.AnonCartRepository;
import com.cs308.backend.repo.ProductRepository;

@Service
public class AnonCartService {
    private final AnonCartRepository anonCartRepository;
    private final ProductRepository productRepository;

    public AnonCartService(AnonCartRepository anonCartRepository, ProductRepository productRepository) {
        this.anonCartRepository = anonCartRepository;
        this.productRepository = productRepository;
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

            Optional<Product> product = productRepository.findById(productId);

            if (!(product.isPresent())) {
                AnonCart updatedAnonCart = anonCartRepository.save(newAnonCart);
                return Optional.of(updatedAnonCart);
            }

            Product foundProduct = product.get();

            if (foundProduct.getQuantityInStock() < quantity) {
                newAnonCart = anonCartRepository.save(newAnonCart);
                return Optional.of(newAnonCart);
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
                e.printStackTrace();
                newAnonCart = anonCartRepository.save(newAnonCart);
                return Optional.of(newAnonCart);
            }
        }
        else {
            AnonCart foundAnonCart = anonCart.get();

            if (foundAnonCart.getItems() == null) {
                foundAnonCart.setItems(new ArrayList<>());
            }

            Optional<Product> product = productRepository.findById(productId);

            if (!(product.isPresent())) {
                foundAnonCart = anonCartRepository.save(foundAnonCart);
                return Optional.of(foundAnonCart);
            }

            Product foundProduct = product.get();

            AnonCart oldAnonCart = foundAnonCart.clone();

            if (foundProduct.getQuantityInStock() < quantity) {
                oldAnonCart = anonCartRepository.save(oldAnonCart);
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

            foundAnonCart.getItems().clear();
            foundAnonCart.getItems().addAll(setOfAnonCartItems);
            foundAnonCart.setTotalPrice(foundAnonCart.getItems().stream()
                .map(item -> item.getPriceAtAddition().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
            foundAnonCart.setUpdatedAt(LocalDateTime.now());

            try {
                AnonCart updatedAnonCart = anonCartRepository.save(foundAnonCart);
                return Optional.of(updatedAnonCart);
            }
            catch (Exception e) {
                e.printStackTrace();
                oldAnonCart = anonCartRepository.save(oldAnonCart);
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

        Optional<Product> product = productRepository.findById(productId);

        if (!(product.isPresent())) {
            foundAnonCart = anonCartRepository.save(foundAnonCart);
            return Optional.of(foundAnonCart);
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

            foundAnonCart.getItems().clear();
            foundAnonCart.getItems().addAll(setOfAnonCartItems);
            foundAnonCart.setTotalPrice(foundAnonCart.getTotalPrice().subtract(anonCartItem.getPriceAtAddition().multiply(BigDecimal.valueOf(anonCartItem.getQuantity()))));
            foundAnonCart.setUpdatedAt(LocalDateTime.now());

            try {
                AnonCart updatedAnonCart = anonCartRepository.save(foundAnonCart);
                return Optional.of(updatedAnonCart);
            }
            catch (Exception e) {
                e.printStackTrace();
                oldAnonCart = anonCartRepository.save(oldAnonCart);
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

            foundAnonCart.getItems().clear();
            foundAnonCart.getItems().addAll(setOfAnonCartItems);
            foundAnonCart.setTotalPrice(foundAnonCart.getItems().stream()
                .map(item -> item.getPriceAtAddition().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
            foundAnonCart.setUpdatedAt(LocalDateTime.now());

            try {
                AnonCart updatedAnonCart = anonCartRepository.save(foundAnonCart);
                return Optional.of(updatedAnonCart);
            }
            catch (Exception e) {
                e.printStackTrace();
                oldAnonCart = anonCartRepository.save(oldAnonCart);
                return Optional.of(oldAnonCart);
            }
        }
        else {
            oldAnonCart = anonCartRepository.save(oldAnonCart);
            return Optional.of(oldAnonCart);
        }
    }
}