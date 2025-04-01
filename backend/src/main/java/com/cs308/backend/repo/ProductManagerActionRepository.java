package com.cs308.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.ProductManagerAction;

public interface ProductManagerActionRepository extends JpaRepository<ProductManagerAction, Long> {}
