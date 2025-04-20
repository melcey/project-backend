package com.cs308.backend.repo;

import com.cs308.backend.dao.AnonCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnonCartItemRepository extends JpaRepository<AnonCartItem, Long> {
    List<AnonCartItem> findByCartId(Long cartId);
}