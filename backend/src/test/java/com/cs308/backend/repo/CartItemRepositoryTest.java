package com.cs308.backend.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
import com.cs308.backend.dao.CartItem;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;

@DataJpaTest
@Testcontainers
public class CartItemRepositoryTest {
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
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByCartId() {
        // Create a new User
        User user = new User("John Doe", "123 Main St", Role.customer);
        User savedUser = userRepository.save(user);

        // Create a new Cart
        Cart cart = new Cart(savedUser);
        cart.setTotalPrice(new BigDecimal("150.00"));
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        Cart savedCart = cartRepository.save(cart);

        // Create CartItems
        CartItem item1 = new CartItem(savedCart, null, 2, new BigDecimal("50.00"));
        item1.setCreatedAt(LocalDateTime.now());

        CartItem item2 = new CartItem(savedCart, null, 3, new BigDecimal("100.00"));
        item2.setCreatedAt(LocalDateTime.now());

        // Save CartItems
        cartItemRepository.save(item1);
        cartItemRepository.save(item2);

        // Fetch items by cart ID
        List<CartItem> fetchedItems = cartItemRepository.findByCartId(savedCart.getId());

        // Assert that the items are retrieved correctly
        assertEquals(2, fetchedItems.size());
        assertTrue(fetchedItems.stream().anyMatch(item -> item.getQuantity() == 2));
        assertTrue(fetchedItems.stream().anyMatch(item -> item.getQuantity() == 3));
    }

    @Test
    public void testSaveAndRetrieveCartItem() {
        // Create a new User
        User user = new User("Jane Doe", "456 Elm St", Role.customer);
        User savedUser = userRepository.save(user);

        // Create a new Cart
        Cart cart = new Cart(savedUser);
        cart.setTotalPrice(new BigDecimal("200.00"));
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        Cart savedCart = cartRepository.save(cart);

        // Create a CartItem
        CartItem item = new CartItem(savedCart, null, 5, new BigDecimal("75.00"));
        item.setCreatedAt(LocalDateTime.now());

        // Save the CartItem
        CartItem savedItem = cartItemRepository.save(item);

        // Fetch the CartItem by ID
        CartItem fetchedItem = cartItemRepository.findById(savedItem.getId()).orElse(null);

        // Assert that the item is saved and retrieved correctly
        assertEquals(savedItem.getId(), fetchedItem.getId());
        assertEquals(savedItem.getQuantity(), fetchedItem.getQuantity());
        assertEquals(savedItem.getPriceAtAddition(), fetchedItem.getPriceAtAddition());
    }

    @AfterEach
    void cleanUp() {
        cartRepository.deleteAll();
        cartItemRepository.deleteAll();
        userRepository.deleteAll();
    }

	@AfterAll
	static void tearDown() {
		postgresContainer.close();
	}
}