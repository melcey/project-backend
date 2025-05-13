package com.cs308.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Category;
import com.cs308.backend.repo.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<Category> createNewCategory(String name, String description) {
        try {
            Category newCategory = new Category(name, description);

            newCategory = categoryRepository.save(newCategory);
            return Optional.of(newCategory);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void deleteCategory(Long categoryId) {
        Optional<Category> retrievedCategory = categoryRepository.findById(categoryId);

        retrievedCategory.ifPresent(categoryRepository::delete);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public List<Category> findCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    public List<Category> findCategoriesByNameContains(String name) {
        return categoryRepository.findByNameContains(name);
    }
}
