package com.cs308.backend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.Category;

// Extending both JpaRepository<Category, Long> for findBy... queries
// and CategoryRepositoryObj to be able to deal with Category objects passed as parameters
public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryObj {
    // SELECT queries:
    List<Category> findByName(String name);
    List<Category> findByNameContains(String name);
    Optional<Category> findById(Long id);
}
