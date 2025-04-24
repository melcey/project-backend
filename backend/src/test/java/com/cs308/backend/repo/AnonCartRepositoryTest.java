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

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.dao.AnonCartItem;
import com.cs308.backend.dao.Product;

@DataJpaTest
@Testcontainers
public class AnonCartRepositoryTest {
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
    private AnonCartRepository anonCartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindById() {
        // Create a new AnonCart
        AnonCart cart = new AnonCart();
        cart.setTotalPrice(new BigDecimal("100.00"));
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        // Save the cart
        AnonCart savedCart = anonCartRepository.save(cart);

        // Fetch the cart by ID
        Optional<AnonCart> fetchedCart = anonCartRepository.findById(savedCart.getId());

        // Assert that the cart is found and matches the saved cart
        assertTrue(fetchedCart.isPresent());
        assertEquals(savedCart.getId(), fetchedCart.get().getId());
        assertEquals(savedCart.getTotalPrice(), fetchedCart.get().getTotalPrice());
    }

    @Test
    public void testSaveAndRetrieveCartWithItems() {
        // Create a new Product
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setQuantityInStock(10);
        product = productRepository.save(product);

        // Create a new AnonCart
        AnonCart cart = new AnonCart();
        cart.setTotalPrice(new BigDecimal("100.00"));
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        // Create a new AnonCartItem
        AnonCartItem item = new AnonCartItem();
        item.setCart(cart);
        item.setQuantity(2);
        item.setPriceAtAddition(new BigDecimal("50.00"));
        item.setProduct(product);
        item.setCreatedAt(LocalDateTime.now());

        // Add the item to the cart
        cart.getItems().add(item);

        // Save the cart
        AnonCart savedCart = anonCartRepository.save(cart);

        // Fetch the cart by ID
        Optional<AnonCart> fetchedCart = anonCartRepository.findById(savedCart.getId());

        // Assert that the cart and its items are saved and retrieved correctly
        assertTrue(fetchedCart.isPresent());
        assertEquals(1, fetchedCart.get().getItems().size());
        assertEquals(new BigDecimal("100.00"), fetchedCart.get().getTotalPrice());
    }

    @AfterEach
    void cleanUp() {
        anonCartRepository.deleteAll();
    }

	@AfterAll
	static void tearDown() {
		postgresContainer.close();
	}
}