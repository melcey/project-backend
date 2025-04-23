package com.cs308.backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);
}