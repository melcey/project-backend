package com.cs308.backend.service;

import java.util.List;

import com.cs308.backend.dao.User;
import com.cs308.backend.dto.WishlistRequest;
import com.cs308.backend.dto.WishlistResponse;

public interface WishlistService {
    void addToWishlist(User user, WishlistRequest request);
    List<WishlistResponse> getWishlist(User user);
    void removeFromWishlist(User user, Long productId);
}
