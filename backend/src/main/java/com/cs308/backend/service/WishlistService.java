package com.cs308.backend.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.dao.Wishlist;
import com.cs308.backend.dao.WishlistItem;
import com.cs308.backend.repo.ProductRepository;
import com.cs308.backend.repo.UserRepository;
import com.cs308.backend.repo.WishlistItemRepository;
import com.cs308.backend.repo.WishlistRepository;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishlistService(WishlistRepository wishlistRepository, WishlistItemRepository wishlistItemRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.wishlistRepository = wishlistRepository;
        this.wishlistItemRepository = wishlistItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Optional<Wishlist> createWishlist(User user) {
        Optional<Wishlist> retrievedWishlist = wishlistRepository.findByUser(user);

        if (retrievedWishlist.isPresent()) {
            return retrievedWishlist;
        }

        Wishlist newWishlist = new Wishlist(user);

        try {
            Wishlist createdWishlist = wishlistRepository.save(newWishlist);
            return Optional.of(createdWishlist);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Wishlist> addToWishlist(User user, Long productId) {
        Optional<Wishlist> retrievedWishlist = wishlistRepository.findByUser(user);

        if (retrievedWishlist.isEmpty()) {
            Wishlist newWishlist = new Wishlist(user);

            Optional<Product> productOpt = productRepository.findById(productId);
        
            if (productOpt.isEmpty()) {
                newWishlist = wishlistRepository.save(newWishlist);
                return Optional.of(newWishlist);
            }

            Product foundProduct = productOpt.get();

            WishlistItem wishlistItem = new WishlistItem();
            
            wishlistItem.setWishlist(newWishlist);
            wishlistItem.setProduct(foundProduct);

            try {
                Wishlist updatedWishlist = wishlistRepository.save(newWishlist);
                return Optional.of(updatedWishlist);
            }
            catch (Exception e) {
                e.printStackTrace();
                newWishlist = wishlistRepository.save(newWishlist);
                return Optional.of(newWishlist);
            }

        }
        else {
            Wishlist foundWishlist = retrievedWishlist.get();

            Optional<Product> productOpt = productRepository.findById(productId);
        
            if (productOpt.isEmpty()) {
                foundWishlist = wishlistRepository.save(foundWishlist);
                return Optional.of(foundWishlist);
            }

            Product foundProduct = productOpt.get();

            // Do not add an existing item into a wishlist again
            Optional<WishlistItem> retrievedWishlistItem = wishlistItemRepository.findByWishlistAndProduct(foundWishlist, foundProduct);

            if (retrievedWishlistItem.isPresent()) {
                foundWishlist = wishlistRepository.save(foundWishlist);
                return Optional.of(foundWishlist);
            }
            
            Wishlist oldWishlist = foundWishlist.clone();

            WishlistItem wishlistItem = new WishlistItem();
            
            wishlistItem.setWishlist(foundWishlist);
            wishlistItem.setProduct(foundProduct);

            Set<WishlistItem> setOfWishlistItems = new LinkedHashSet<>(foundWishlist.getWishlistItems());
            setOfWishlistItems.add(wishlistItem);

            foundWishlist.getWishlistItems().clear();
            foundWishlist.getWishlistItems().addAll(setOfWishlistItems);

            try {
                Wishlist updatedWishlist = wishlistRepository.save(foundWishlist);
                return Optional.of(updatedWishlist);
            }
            catch (Exception e) {
                e.printStackTrace();
                oldWishlist = wishlistRepository.save(oldWishlist);
                return Optional.of(oldWishlist);
            }
        }
    }

    public Optional<Wishlist> getWishlist(User user) {
        return wishlistRepository.findByUser(user);
    }

    public Optional<Wishlist> removeFromWishlist(User user, Long productId) {
        Optional<Wishlist> retrievedWishlist = wishlistRepository.findByUser(user);

        if (!(retrievedWishlist.isPresent())) {
            Wishlist newWishlist = new Wishlist(user);

            newWishlist = wishlistRepository.save(newWishlist);
            return Optional.of(newWishlist);
        }
        else {
            Wishlist foundWishlist = retrievedWishlist.get();

            Optional<Product> productOpt = productRepository.findById(productId);
        
            if (productOpt.isEmpty()) {
                foundWishlist = wishlistRepository.save(foundWishlist);
                return Optional.of(foundWishlist);
            }

            Product foundProduct = productOpt.get();

            // Do not remove a nonexistent item from a wishlist
            Optional<WishlistItem> retrievedWishlistItem = wishlistItemRepository.findByWishlistAndProduct(foundWishlist, foundProduct);

            if (!(retrievedWishlistItem.isPresent())) {
                foundWishlist = wishlistRepository.save(foundWishlist);
                return Optional.of(foundWishlist);
            }

            Wishlist oldWishlist = foundWishlist.clone();

            WishlistItem wishlistItem = retrievedWishlistItem.get();

            Set<WishlistItem> setOfWishlistItems = new LinkedHashSet<>(foundWishlist.getWishlistItems());
            setOfWishlistItems.remove(wishlistItem);

            foundWishlist.getWishlistItems().clear();
            foundWishlist.getWishlistItems().addAll(setOfWishlistItems);

            try {
                Wishlist updatedWishlist = wishlistRepository.save(foundWishlist);
                return Optional.of(updatedWishlist);
            }
            catch (Exception e) {
                e.printStackTrace();
                oldWishlist = wishlistRepository.save(oldWishlist);
                return Optional.of(oldWishlist);
            }
        }
    }

    public Optional<Wishlist> clearWishlist(User user) {
        Optional<Wishlist> retrievedWishlist = wishlistRepository.findByUser(user);

        if (!(retrievedWishlist.isPresent())) {
            Wishlist newWishlist = new Wishlist(user);

            newWishlist = wishlistRepository.save(newWishlist);
            return Optional.of(newWishlist);
        }

        Wishlist foundWishlist = retrievedWishlist.get();

        if (foundWishlist.getWishlistItems() == null) {
            foundWishlist.setWishlistItems(new ArrayList<>());
        }

        Wishlist oldWishlist = foundWishlist.clone();

        foundWishlist.getWishlistItems().clear();

        try {
            Wishlist updatedWishlist = wishlistRepository.save(foundWishlist);
            return Optional.of(updatedWishlist);
        }
        catch (Exception e) {
            e.printStackTrace();
            oldWishlist = wishlistRepository.save(oldWishlist);
            return Optional.of(oldWishlist);
        }
    }

    public List<User> getUsersWithProductInWishlist(Long productId) {
        Set<User> users = new LinkedHashSet<>();

        Optional<Product> productOpt = productRepository.findById(productId);
        
        if (productOpt.isEmpty()) {
            return new ArrayList<>(users);
        }

        Product foundProduct = productOpt.get();

        List<WishlistItem> foundWishlistItems = wishlistItemRepository.findAllByProduct(foundProduct);

        if (foundWishlistItems.size() == 0) {
            return new ArrayList<>(users);
        }

        for (WishlistItem foundWishlistItem: foundWishlistItems) {
            Optional<Wishlist> foundWishlist = wishlistRepository.findByWishlistItemsContains(foundWishlistItem);

            if (foundWishlist.isPresent()) {
                User currentUser = foundWishlist.get().getUser();

                Optional<User> retrievedUser = userRepository.findById(currentUser.getId());

                if (retrievedUser.isPresent() && retrievedUser.get().equals(currentUser)) {
                    users.add(currentUser);
                }
            }
        }

        return new ArrayList<>(users);
    }
}
