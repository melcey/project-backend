package com.cs308.backend.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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

import com.cs308.backend.dao.ProductManagerAction;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;

@DataJpaTest
@Testcontainers
public class ProductManagerActionRepositoryTest {
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
    private ProductManagerActionRepository productManagerActionRepository;

    @Autowired
    private UserRepository userRepository;

    private ProductManagerAction testAction;
    private User productManager;

    @BeforeEach
    void setUp() {
        // Create and save a User entity
        productManager = new User();
        productManager.setName("John Doe");
        productManager.setRole(Role.product_manager);
        userRepository.save(productManager);

        // Initialize a test ProductManagerAction
        testAction = new ProductManagerAction();
        testAction.setProductManager(productManager);
        testAction.setActionType("ADD_PRODUCT");
        testAction.setDetails("Added a new product to the catalog.");
        testAction.setActionDate(LocalDateTime.now());
        productManagerActionRepository.save(testAction);
    }

    @Test
    void testSaveProductManagerAction() {
        ProductManagerAction newAction = new ProductManagerAction();
        newAction.setActionType("UPDATE_PRODUCT");
        newAction.setDetails("Updated product details.");
        newAction.setActionDate(LocalDateTime.now());
        newAction.setProductManager(productManager);

        ProductManagerAction savedAction = productManagerActionRepository.save(newAction);

        assertThat(savedAction.getActionId()).isNotNull();
        assertThat(savedAction.getActionType()).isEqualTo("UPDATE_PRODUCT");
    }

    @Test
    void testFindById() {
        Optional<ProductManagerAction> foundAction = productManagerActionRepository.findById(testAction.getActionId());
        assertThat(foundAction).isPresent();
        assertThat(foundAction.get().getActionType()).isEqualTo("ADD_PRODUCT");
    }

    @Test
    void testFindAll() {
        Iterable<ProductManagerAction> actions = productManagerActionRepository.findAll();
        assertThat(actions).hasSize(1);
    }

    @Test
    void testDeleteById() {
        productManagerActionRepository.deleteById(testAction.getActionId());
        Optional<ProductManagerAction> deletedAction = productManagerActionRepository.findById(testAction.getActionId());
        assertThat(deletedAction).isNotPresent();
    }

    @Test
    void testUpdateProductManagerAction() {
        testAction.setDetails("Updated the product description.");
        ProductManagerAction updatedAction = productManagerActionRepository.save(testAction);

        assertThat(updatedAction.getDetails()).isEqualTo("Updated the product description.");
    }

    @AfterEach
    void cleanUp() {
        productManagerActionRepository.deleteAll();
        userRepository.deleteAll();
    }

	@AfterAll
	static void tearDown() {
		postgresContainer.close();
	}
}