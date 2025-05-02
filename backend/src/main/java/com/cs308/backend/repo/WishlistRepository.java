package com.cs308.backend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.Wishlist;
import com.cs308.backend.dao.User;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.WishlistItem;


public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByUser(User user);

    Optional<Wishlist> findByWishlistItemsContains(WishlistItem wishlistItem);
}
