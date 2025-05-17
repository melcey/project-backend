package com.cs308.backend;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cs308.backend.controller.AnonCartController;
import com.cs308.backend.controller.AuthController;
import com.cs308.backend.controller.CartController;
import com.cs308.backend.controller.CommentController;
import com.cs308.backend.controller.InvoiceController;
import com.cs308.backend.controller.OrderController;
import com.cs308.backend.controller.PaymentController;
import com.cs308.backend.controller.ProductController;
import com.cs308.backend.controller.ProductManagerController;
import com.cs308.backend.controller.RatingController;
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
import com.cs308.backend.security.CustomAuthenticationProvider;
import com.cs308.backend.security.CustomUserDetailsService;
import com.cs308.backend.security.JwtAuthenticationFilter;
import com.cs308.backend.security.JwtSecurityConfig;
import com.cs308.backend.security.JwtTokenProvider;
import com.cs308.backend.service.AnonCartService;
import com.cs308.backend.service.CartService;
import com.cs308.backend.service.CommentService;
import com.cs308.backend.service.EmailService;
import com.cs308.backend.service.InvoiceService;
import com.cs308.backend.service.OrderService;
import com.cs308.backend.service.PaymentService;
import com.cs308.backend.service.ProductManagerActionService;
import com.cs308.backend.service.ProductService;
import com.cs308.backend.service.RatingService;
import com.cs308.backend.service.UserService;

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

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private ProductManagerActionRepository productManagerActionRepository;

	@Autowired
    private ProductRepository productRepository;

	@Autowired
	private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

	@Autowired
	private AnonCartService anonCartService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private ProductManagerActionService productManagerActionService;

	@Autowired
	private ProductService productService;

	@Autowired
	private RatingService ratingService;

	@Autowired
	private UserService userService;

	@Autowired
	private AnonCartController anonCartController;

	@Autowired
	private AuthController authController;

	@Autowired
	private CartController cartController;

	@Autowired
	private CommentController commentController;

	@Autowired
	private InvoiceController invoiceController;

	@Autowired
	private OrderController orderController;

	@Autowired
	private PaymentController paymentController;

	@Autowired
	private ProductController productController;

	@Autowired
	private ProductManagerController productManagerController;

	@Autowired
	private RatingController ratingController;

	@Autowired
	private CustomAuthenticationProvider authenticationProvider;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtAuthenticationFilter authenticationFilter;

	@Autowired
	private JwtSecurityConfig securityConfig;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

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
		Assertions.assertNotNull(invoiceRepository);
		Assertions.assertNotNull(orderItemRepository);
		Assertions.assertNotNull(orderRepository);
		Assertions.assertNotNull(paymentRepository);
		Assertions.assertNotNull(productManagerActionRepository);
		Assertions.assertNotNull(productRepository);
		Assertions.assertNotNull(ratingRepository);
		Assertions.assertNotNull(userRepository);
    }

	@Test
	// Tests whether the service beans exist as non-null objects when the application runs
    void testServiceBeansPresent() {
		Assertions.assertNotNull(anonCartService);
		Assertions.assertNotNull(cartService);
		Assertions.assertNotNull(commentService);
		Assertions.assertNotNull(emailService);
		Assertions.assertNotNull(invoiceService);
		Assertions.assertNotNull(orderService);
		Assertions.assertNotNull(paymentService);
		Assertions.assertNotNull(productManagerActionService);
		Assertions.assertNotNull(productService);
		Assertions.assertNotNull(ratingService);
		Assertions.assertNotNull(userService);
    }

	@Test
	// Tests whether the controller beans exist as non-null objects when the application runs
    void testControllerBeansPresent() {
		Assertions.assertNotNull(anonCartController);
		Assertions.assertNotNull(authController);
		Assertions.assertNotNull(cartController);
		Assertions.assertNotNull(commentController);
		Assertions.assertNotNull(invoiceController);
		Assertions.assertNotNull(orderController);
		Assertions.assertNotNull(paymentController);
		Assertions.assertNotNull(productController);
		Assertions.assertNotNull(productManagerController);
		Assertions.assertNotNull(ratingController);
    }

	@Test
	// Tests whether the security beans exist as non-null objects when the application runs
    void testSecurityBeansPresent() {
		Assertions.assertNotNull(authenticationProvider);
		Assertions.assertNotNull(userDetailsService);
		Assertions.assertNotNull(authenticationFilter);
		Assertions.assertNotNull(securityConfig);
		Assertions.assertNotNull(jwtTokenProvider);
    }

	// Runs after all the tests are completed
	@AfterAll
	static void tearDown() {
		// Closes the mock PostgreSQL database container
		postgresContainer.close();
	}
}
