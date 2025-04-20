package com.cs308.backend.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cs308.backend.dao.Category;

@DataJpaTest
@Testcontainers
public class CategoryRepositoryTest {
    @SuppressWarnings("resource")
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
        .withDatabaseName("testdb")
        .withUsername("testuser")
        .withPassword("testpass");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private CategoryRepository categoryRepository;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        // Initialize a test category
        testCategory = new Category();
        testCategory.setName("Test Category");
        testCategory.setDescription("Test Description");
        categoryRepository.save(testCategory);
    }

    @Test
    void testFindByName() {
        List<Category> categories = categoryRepository.findByName("Test Category");
        assertThat(categories).isNotEmpty();
        assertThat(categories.get(0).getName()).isEqualTo("Test Category");
    }

    @Test
    void testFindByNameContains() {
        List<Category> categories = categoryRepository.findByNameContains("Test");
        assertThat(categories).isNotEmpty();
        assertThat(categories.get(0).getName()).contains("Test");
    }

    @Test
    void testFindById() {
        Optional<Category> category = categoryRepository.findById(testCategory.getId());
        assertThat(category).isPresent();
        assertThat(category.get().getName()).isEqualTo("Test Category");
    }

    @Test
    void testInsertNewCategory() {
        Category newCategory = new Category();
        newCategory.setName("New Category");
        newCategory.setDescription("New Description");

        Optional<Category> savedCategory = categoryRepository.insertNewCategory(newCategory);
        assertThat(savedCategory).isPresent();
        assertThat(savedCategory.get().getName()).isEqualTo("New Category");
    }

    @Test
    void testDeleteCategory() {
        categoryRepository.deleteCategory(testCategory);
        Optional<Category> deletedCategory = categoryRepository.findById(testCategory.getId());
        assertThat(deletedCategory).isNotPresent();
    }

    @Test
    void testUpdateCategoryName() {
        Optional<Category> updatedCategory = categoryRepository.updateCategoryName(testCategory, "Updated Name");
        assertThat(updatedCategory).isPresent();
        assertThat(updatedCategory.get().getName()).isEqualTo("Updated Name");
    }

    @Test
    void testUpdateCategoryDescription() {
        Optional<Category> updatedCategory = categoryRepository.updateCategoryDescription(testCategory, "Updated Description");
        assertThat(updatedCategory).isPresent();
        assertThat(updatedCategory.get().getDescription()).isEqualTo("Updated Description");
    }

    @AfterEach
    void cleanUp() {
        categoryRepository.deleteAll();
    }

	@AfterAll
	static void tearDown() {
		postgresContainer.close();
	}
}