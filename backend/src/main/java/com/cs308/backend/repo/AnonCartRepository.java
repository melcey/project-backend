package com.cs308.backend.repo;

import com.cs308.backend.dao.AnonCart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AnonCartRepository extends JpaRepository<AnonCart, Long> {
    Optional<AnonCart> findById(Long id);
}