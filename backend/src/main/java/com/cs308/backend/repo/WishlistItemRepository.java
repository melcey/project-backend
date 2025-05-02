package com.cs308.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Wishlist;
import com.cs308.backend.dao.WishlistItem;
import java.util.List;
import java.util.Optional;


public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findAllByProduct(Product product);

    List<WishlistItem> findByWishlist(Wishlist wishlist);

    Optional<WishlistItem> findByWishlistAndProduct(Wishlist wishlist, Product product);
}
