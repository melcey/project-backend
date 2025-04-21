package com.cs308.backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.AnonCart;

public interface AnonCartRepository extends JpaRepository<AnonCart, Long> {
    Optional<AnonCart> findById(Long id);
}