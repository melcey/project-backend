package com.cs308.backend;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cs308.backend.dao.Category;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.AnonCartItemRepository;
import com.cs308.backend.repo.AnonCartRepository;
import com.cs308.backend.repo.CartItemRepository;
import com.cs308.backend.repo.CartRepository;
import com.cs308.backend.repo.CategoryRepository;
import com.cs308.backend.repo.CommentRepository;
import com.cs308.backend.repo.CreditCardRepository;
import com.cs308.backend.repo.InvoiceRepository;
import com.cs308.backend.repo.OrderItemRepository;
import com.cs308.backend.repo.OrderRepository;
import com.cs308.backend.repo.PaymentRepository;
import com.cs308.backend.repo.ProductManagerActionRepository;
import com.cs308.backend.repo.ProductRepository;
import com.cs308.backend.repo.RatingRepository;
import com.cs308.backend.repo.UserRepository;

// Specifies the configuration class as the main application of the backend
@SpringBootTest(classes = BackendApplication.class)
// Testcontainers will be used in order to test the database-related stuff
@Testcontainers
class BackendApplicationTest {

	@SuppressWarnings("resource")
	@Container
	// Creates a Testcontainers container to be used as a mock PostgreSQL database
	static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
		// The connection info for the mock database
		.withDatabaseName("testdb")
		.withUsername("testuser")
		.withPassword("testpass");

	@DynamicPropertySource
	// Dynamically overrides the Spring configuration in order to connect to the mock database for tests
	static void overrideProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgresContainer::getUsername);
		registry.add("spring.datasource.password", postgresContainer::getPassword);
	}

	@Autowired
    private DataSource dataSource;

	@Autowired
	private AnonCartItemRepository anonCartItemRepository;

	@Autowired
	private AnonCartRepository anonCartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
    private CategoryRepository categoryRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private CreditCardRepository creditCardRepository;

	//@Autowired
	//private InvoiceRepository invoiceRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	//@Autowired
	//private PaymentRepository paymentRepository;

	@Autowired
	private ProductManagerActionRepository productManagerActionRepository;

	@Autowired
    private ProductRepository productRepository;

	@Autowired
	private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

	@Test
	// Tests whether the application context is loaded successfully.
	void contextLoads() {}

	@Test
	// Tests whether the application can connect to the database on the mock database container
	void testDatabaseConnection() throws SQLException {
		// Tries to connect to the mock database container
		try (Connection connection = dataSource.getConnection()) {
			// Checks whether the connection is valid or not, with a timeout of 2 seconds 
			Assertions.assertTrue(connection.isValid(2), "The connection should be valid");
		}
	}

    @Test
	// Tests whether the repository beans exist as non-null objects when the application runs
    void testRepositoryBeansPresent() {
		Assertions.assertNotNull(anonCartItemRepository);
		Assertions.assertNotNull(anonCartRepository);
		Assertions.assertNotNull(cartItemRepository);
		Assertions.assertNotNull(cartRepository);
		Assertions.assertNotNull(categoryRepository);
		Assertions.assertNotNull(commentRepository);
		Assertions.assertNotNull(creditCardRepository);
		//Assertions.assertNotNull(invoiceRepository);
		Assertions.assertNotNull(orderItemRepository);
		Assertions.assertNotNull(orderRepository);
		//Assertions.assertNotNull(paymentRepository);
		Assertions.assertNotNull(productManagerActionRepository);
		Assertions.assertNotNull(productRepository);
		Assertions.assertNotNull(ratingRepository);
		Assertions.assertNotNull(userRepository);
    }

	// Clears the test data for each repository after each test
	@AfterEach
    void cleanUp() {
		anonCartItemRepository.deleteAll();
		anonCartRepository.deleteAll();
		cartItemRepository.deleteAll();
		cartRepository.deleteAll();
		categoryRepository.deleteAll();
		commentRepository.deleteAll();
		creditCardRepository.deleteAll();
		//invoiceRepository.deleteAll();
		orderItemRepository.deleteAll();
		orderRepository.deleteAll();
		//paymentRepository.deleteAll();
		productManagerActionRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

	// Runs after all the tests are completed
	@AfterAll
	static void tearDown() {
		// Closes the mock PostgreSQL database container
		postgresContainer.close();
	}
}
