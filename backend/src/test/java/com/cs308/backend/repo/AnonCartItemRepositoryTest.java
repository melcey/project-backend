package com.cs308.backend.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

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

import com.cs308.backend.dao.AnonCart;
import com.cs308.backend.dao.AnonCartItem;
import com.cs308.backend.dao.Product;

@DataJpaTest
@Testcontainers
public class AnonCartItemRepositoryTest {
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
    private AnonCartItemRepository anonCartItemRepository;

    @Autowired
    private AnonCartRepository anonCartRepository;

    @Autowired
    private ProductRepository productRepository;

    private AnonCart cart;
    private Product product;

    @BeforeEach
    public void setUp() {
        // Create and save a cart
        cart = new AnonCart();
        cart = anonCartRepository.save(cart);

        // Create and save a product
        product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setQuantityInStock(10);
        product = productRepository.save(product);

        // Create and save cart items
        AnonCartItem item1 = new AnonCartItem(cart, product, 2, BigDecimal.valueOf(100.00));
        AnonCartItem item2 = new AnonCartItem(cart, product, 1, BigDecimal.valueOf(100.00));
        anonCartItemRepository.save(item1);
        anonCartItemRepository.save(item2);
    }

    @Test
    public void testFindByCartId() {
        // Retrieve items by cart ID
        List<AnonCartItem> items = anonCartItemRepository.findByCartId(cart.getId());

        // Assertions
        assertThat(items).isNotNull();
        assertThat(items.size()).isEqualTo(2);
        assertThat(items.get(0).getCart().getId()).isEqualTo(cart.getId());
        assertThat(items.get(0).getProduct().getId()).isEqualTo(product.getId());
    }

    @Test
    public void testFindByCartId_NoItems() {
        // Retrieve items for a non-existent cart ID
        List<AnonCartItem> items = anonCartItemRepository.findByCartId(-1L);

        // Assertions
        assertThat(items).isEmpty();
    }

    @AfterEach
    void cleanUp() {
        anonCartItemRepository.deleteAll();
        anonCartRepository.deleteAll();
        productRepository.deleteAll();
    }

	@AfterAll
	static void tearDown() {
		postgresContainer.close();
	}
}