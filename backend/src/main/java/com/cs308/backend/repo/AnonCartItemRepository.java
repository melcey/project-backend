package com.cs308.backend.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.AnonCartItem;

public interface AnonCartItemRepository extends JpaRepository<AnonCartItem, Long> {
    List<AnonCartItem> findByCartId(Long cartId);
}