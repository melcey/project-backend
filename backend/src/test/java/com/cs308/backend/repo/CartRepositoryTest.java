package com.cs308.backend.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cs308.backend.dao.Cart;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;

@DataJpaTest
@Testcontainers
public class CartRepositoryTest {
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
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUser() {
        // Create a new User
        User user = new User("John Doe", "123 Main St", Role.customer, "taxId");
        User savedUser = userRepository.save(user);

        // Create a new Cart
        Cart cart = new Cart(savedUser);
        cart.setTotalPrice(new BigDecimal("150.00"));
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        cart.setUser(savedUser);

        // Save the Cart
        Cart savedCart = cartRepository.save(cart);

        // Fetch the Cart by User
        Optional<Cart> fetchedCart = cartRepository.findByUser(savedUser);

        // Assert that the Cart is retrieved correctly
        assertTrue(fetchedCart.isPresent());
        assertEquals(savedCart.getId(), fetchedCart.get().getId());
        assertEquals(savedCart.getTotalPrice(), fetchedCart.get().getTotalPrice());
    }

    @Test
    public void testSaveAndRetrieveCart() {
        // Create a new User
        User user = new User("Jane Doe", "456 Elm St", Role.customer, "taxId");
        User savedUser = userRepository.save(user);

        // Create a new Cart
        Cart cart = new Cart(savedUser);
        cart.setTotalPrice(new BigDecimal("200.00"));
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        // Save the Cart
        Cart savedCart = cartRepository.save(cart);

        // Fetch the Cart by ID
        Optional<Cart> fetchedCart = cartRepository.findById(savedCart.getId());

        // Assert that the Cart is saved and retrieved correctly
        assertTrue(fetchedCart.isPresent());
        assertEquals(savedCart.getId(), fetchedCart.get().getId());
        assertEquals(savedCart.getTotalPrice(), fetchedCart.get().getTotalPrice());
    }

    @AfterEach
    void cleanUp() {
        cartRepository.deleteAll();
        userRepository.deleteAll();
    }

	@AfterAll
	static void tearDown() {
		postgresContainer.close();
	}
}