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
import com.cs308.backend.repo.CategoryRepository;
import com.cs308.backend.repo.ProductRepository;
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
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

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
		Assertions.assertNotNull(userRepository);
		Assertions.assertNotNull(categoryRepository);
		Assertions.assertNotNull(productRepository);
    }

	@Test
	void testUserInsertion() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);
		Optional<User> inserted = userRepository.insertNewUser(testUser, "test@example.com", "test123");

		Assertions.assertTrue(inserted.isPresent());
		Assertions.assertNotNull(inserted.get().getId());
		Assertions.assertEquals(testUser.getName(), inserted.get().getName());
		Assertions.assertEquals(testUser.getAddress(), inserted.get().getAddress());
		Assertions.assertEquals(testUser.getRole(), inserted.get().getRole());
		
		// Verify that email and password are stored encrypted/hashed
		byte[] encryptedEmail = inserted.get().getEncryptedEmail();
		byte[] passwordHash = inserted.get().getPasswordHashed();
		Assertions.assertNotNull(encryptedEmail, "Encrypted email should not be null");
		Assertions.assertNotNull(passwordHash, "Password hash should not be null");
		Assertions.assertNotEquals("test@example.com", new String(encryptedEmail));
		Assertions.assertNotEquals("password123", new String(passwordHash));
	}

	@Test
	void testUserQueryById() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);
		Optional<User> inserted = userRepository.insertNewUser(testUser, "test@example.com", "test123");

		Optional<User> found = userRepository.findById(inserted.get().getId());
		Assertions.assertTrue(found.isPresent(), "User should be retrievable by ID");
		Assertions.assertEquals(inserted.get().getName(), found.get().getName());
	}

	@Test
	void testUserQueryByEmail() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);
		Optional<User> inserted = userRepository.insertNewUser(testUser, "test@example.com", "test123");
		
		Optional<User> found = userRepository.findByEmail("test@example.com");
		Assertions.assertTrue(found.isPresent(), "User should be retrievable by email");
		Assertions.assertEquals(inserted.get().getName(), found.get().getName());
	}

	@Test
	void testUserQueryByName() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);
		userRepository.insertNewUser(testUser, "test@example.com", "test123");

		List<User> users = userRepository.findByName(testUser.getName());
		Assertions.assertFalse(users.isEmpty(), "At least one user should be returned via name query");
	}

	@Test
	void testUserQueryByAddress() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);
		userRepository.insertNewUser(testUser, "test@example.com", "test123");

		List<User> users = userRepository.findByAddress(testUser.getAddress());
		Assertions.assertFalse(users.isEmpty(), "At least one user should be returned via address query");
	}

	@Test
	void testUserQueryByCredentials() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);
		Optional<User> inserted = userRepository.insertNewUser(testUser, "test@example.com", "test123");

		Optional<User> found = userRepository.findByEmailAndPasswordAndRole("test@example.com", "test123", testUser.getRole().toString());
		Assertions.assertTrue(found.isPresent(), "User should be retrievable by credentials");
		Assertions.assertEquals(inserted.get().getName(), found.get().getName());
	}

	@Test
	void testUserUpdateName() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);
		Optional<User> inserted = userRepository.insertNewUser(testUser, "test@example.com", "test123");

		Optional<User> updated = userRepository.updateUserName(inserted.get(), "Updated Name");
		Assertions.assertEquals("Updated Name", updated.get().getName(), "User name should be updated");
	}

	@Test
	void testUserUpdateEmail() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);
		Optional<User> inserted = userRepository.insertNewUser(testUser, "test@example.com", "test123");

		Optional<User> updated = userRepository.updateUserEmail(inserted.get(), "newtest@example.com");
		// Verify that the stored encrypted email does not match the plain text
		Assertions.assertNotEquals("newtest@example.com", new String(updated.get().getEncryptedEmail()));
	}

	@Test
	void testUserUpdateAddress() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);
		Optional<User> inserted = userRepository.insertNewUser(testUser, "test@example.com", "test123");

		Optional<User> updated = userRepository.updateUserAddress(inserted.get(), "456 New Address");
		Assertions.assertEquals("456 New Address", updated.get().getAddress(), "User address should be updated");
	}

	@Test
	void testUserUpdatePassword() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);
		Optional<User> inserted = userRepository.insertNewUser(testUser, "test@example.com", "test123");

		Optional<User> updated = userRepository.updateUserPassword(inserted.get(), "newPassword456");
		// Verify that the new password is stored hashed/encrypted
		Assertions.assertNotEquals("newPassword456", new String(updated.get().getPasswordHashed()));
	}

	@Test
	void testUserDeletion() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);
		Optional<User> inserted = userRepository.insertNewUser(testUser, "test@example.com", "test123");

		userRepository.deleteUserById(inserted.get());
		Optional<User> found = userRepository.findById(inserted.get().getId());
		Assertions.assertFalse(found.isPresent(), "User should be deleted");
	}

    @Test
	void testCategoryInsertion() {
		Category category = new Category();
		category.setName("Electronics");
		category.setDescription("Electronic gadgets and devices");

		Optional<Category> inserted = categoryRepository.insertNewCategory(category);
		Assertions.assertNotNull(inserted);
		Assertions.assertNotNull(inserted.get().getId());
	}

	@Test
	void testCategoryQueryById() {
		Category category = new Category();
		category.setName("Home");
		category.setDescription("Home appliances");
		Optional<Category> inserted = categoryRepository.insertNewCategory(category);

		Optional<Category> found = categoryRepository.findById(inserted.get().getId());
		Assertions.assertTrue(found.isPresent());
		Assertions.assertEquals("Home", found.get().getName());
	}

	@Test
	void testCategoryQueryByName() {
		Category category = new Category();
		category.setName("Sports");
		category.setDescription("Sports equipment");
		categoryRepository.insertNewCategory(category);

		List<Category> categories = categoryRepository.findByName("Sports");
		Assertions.assertFalse(categories.isEmpty());
	}

	@Test
	void testCategoryUpdateName() {
		Category category = new Category();
		category.setName("Books");
		category.setDescription("Book category");
		Optional<Category> inserted = categoryRepository.insertNewCategory(category);

		Optional<Category> updated = categoryRepository.updateCategoryName(inserted.get(), "Literature");
		Assertions.assertEquals("Literature", updated.get().getName());
	}

	@Test
	void testCategoryUpdateDescription() {
		Category category = new Category();
		category.setName("Clothing");
		category.setDescription("All kinds of clothing");
		Optional<Category> inserted = categoryRepository.insertNewCategory(category);

		Optional<Category> updated = categoryRepository.updateCategoryDescription(inserted.get(), "Men and Women Clothing");
		Assertions.assertEquals("Men and Women Clothing", updated.get().getDescription());
	}

	@Test
	void testCategoryDeletion() {
		Category category = new Category();
		category.setName("Automotive");
		category.setDescription("Automotive parts");
		Optional<Category> inserted = categoryRepository.insertNewCategory(category);

		categoryRepository.deleteCategory(inserted.get());
		Optional<Category> found = categoryRepository.findById(inserted.get().getId());
		Assertions.assertFalse(found.isPresent(), "Category should be deleted");
	}

	@Test
	void testProductInsertion() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);

		Category category = new Category();
		category.setName("Books");
		category.setDescription("Books category");
		Optional<Category> savedCategory = categoryRepository.insertNewCategory(category);
		
		Product product = new Product();
		product.setName("Effective Java");
		product.setModel("3rd Edition");
		product.setSerialNumber("EJ-003");
		product.setDescription("Programming guide");
		product.setPrice(new BigDecimal("45.00"));
		product.setQuantityInStock(12);
		product.setCategory(savedCategory.get());
		product.setProductManager(testUser);

		Optional<Product> inserted = productRepository.insertNewProduct(product);
		Assertions.assertTrue(inserted.isPresent());
		Assertions.assertNotNull(inserted.get().getId());
	}

	@Test
	void testProductQueryById() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);

		Category category = new Category();
		category.setName("Music");
		category.setDescription("Musical instruments");
		Optional<Category> savedCategory = categoryRepository.insertNewCategory(category);
		
		Product product = new Product();
		product.setName("Piano");
		product.setModel("Grand");
		product.setSerialNumber("PN-001");
		product.setDescription("Grand piano");
		product.setPrice(new BigDecimal("5000.00"));
		product.setQuantityInStock(3);
		product.setCategory(savedCategory.get());
		product.setProductManager(testUser);
		Optional<Product> inserted = productRepository.insertNewProduct(product);
		
		Optional<Product> found = productRepository.findById(inserted.get().getId());
		Assertions.assertTrue(found.isPresent());
		Assertions.assertEquals("Piano", found.get().getName());
	}

	@Test
	void testProductUpdateName() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);

		Category category = new Category();
		category.setName("Gadgets");
		category.setDescription("Gadgets and accessories");
		Optional<Category> savedCategory = categoryRepository.insertNewCategory(category);
		
		Product product = new Product();
		product.setName("Old Gadget");
		product.setModel("V1");
		product.setSerialNumber("GD-100");
		product.setDescription("Old description");
		product.setPrice(new BigDecimal("99.99"));
		product.setQuantityInStock(20);
		product.setCategory(savedCategory.get());
		product.setProductManager(testUser);
		Optional<Product> inserted = productRepository.insertNewProduct(product);
		
		Optional<Product> updated = productRepository.updateProductName(inserted.get(), "New Gadget");
		Assertions.assertEquals("New Gadget", updated.get().getName());
	}

	@Test
	void testProductUpdateModel() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);

		Category category = new Category();
		category.setName("Gadgets");
		category.setDescription("Tech gadgets");
		Optional<Category> savedCategory = categoryRepository.insertNewCategory(category);
		
		Product product = new Product();
		product.setName("Smartphone");
		product.setModel("Model X");
		product.setSerialNumber("SP-101");
		product.setDescription("Latest smartphone");
		product.setPrice(new BigDecimal("699.99"));
		product.setQuantityInStock(50);
		product.setCategory(savedCategory.get());
		product.setProductManager(testUser);
		Optional<Product> inserted = productRepository.insertNewProduct(product);
		
		Optional<Product> updated = productRepository.updateProductModel(inserted.get(), "Model Y");
		Assertions.assertEquals("Model Y", updated.get().getModel());
	}

	@Test
	void testProductUpdateSerialNumber() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);

		Category category = new Category();
		category.setName("Appliances");
		category.setDescription("Home appliances");
		Optional<Category> savedCategory = categoryRepository.insertNewCategory(category);
		
		Product product = new Product();
		product.setName("Refrigerator");
		product.setModel("X100");
		product.setSerialNumber("RF-500");
		product.setDescription("Double door refrigerator");
		product.setPrice(new BigDecimal("899.99"));
		product.setQuantityInStock(10);
		product.setCategory(savedCategory.get());
		product.setProductManager(testUser);
		Optional<Product> inserted = productRepository.insertNewProduct(product);
		
		Optional<Product> updated = productRepository.updateProductSerialNumber(inserted.get(), "RF-501");
		Assertions.assertEquals("RF-501", updated.get().getSerialNumber());
	}

	@Test
	void testProductUpdateDescription() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);

		Category category = new Category();
		category.setName("Furniture");
		category.setDescription("Home furniture");
		Optional<Category> savedCategory = categoryRepository.insertNewCategory(category);
		
		Product product = new Product();
		product.setName("Sofa");
		product.setModel("Comfort");
		product.setSerialNumber("SF-300");
		product.setDescription("Old description");
		product.setPrice(new BigDecimal("299.99"));
		product.setQuantityInStock(8);
		product.setCategory(savedCategory.get());
		product.setProductManager(testUser);
		Optional<Product> inserted = productRepository.insertNewProduct(product);
		
		Optional<Product> updated = productRepository.updateProductDescription(inserted.get(), "Updated comfy sofa");
		Assertions.assertEquals("Updated comfy sofa", updated.get().getDescription());
	}

	@Test
	void testProductUpdateQuantity() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);

		Category category = new Category();
		category.setName("Books");
		category.setDescription("Books category");
		Optional<Category> savedCategory = categoryRepository.insertNewCategory(category);
		
		Product product = new Product();
		product.setName("Clean Code");
		product.setModel("1st Edition");
		product.setSerialNumber("CC-007");
		product.setDescription("Software craftsmanship guide");
		product.setPrice(new BigDecimal("39.99"));
		product.setQuantityInStock(5);
		product.setCategory(savedCategory.get());
		product.setProductManager(testUser);
		Optional<Product> inserted = productRepository.insertNewProduct(product);
		
		Optional<Product> updated = productRepository.updateProductQuantityInStock(inserted.get(), 10);
		Assertions.assertEquals(10, updated.get().getQuantityInStock());
	}

	@Test
	void testProductUpdatePrice() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);

		Category category = new Category();
		category.setName("Software");
		category.setDescription("Software products");
		Optional<Category> savedCategory = categoryRepository.insertNewCategory(category);
		
		Product product = new Product();
		product.setName("IntelliJ IDEA");
		product.setModel("Ultimate");
		product.setSerialNumber("IJ-2025");
		product.setDescription("IDE for JVM languages");
		product.setPrice(new BigDecimal("199.99"));
		product.setQuantityInStock(25);
		product.setCategory(savedCategory.get());
		product.setProductManager(testUser);
		Optional<Product> inserted = productRepository.insertNewProduct(product);
		
		Optional<Product> updated = productRepository.updateProductPrice(inserted.get(), new BigDecimal("249.99"));
		Assertions.assertEquals(new BigDecimal("249.99"), updated.get().getPrice());
	}

	@Test
	void testProductDeletion() {
		User testUser = new User();
		testUser.setName("Test User");
		testUser.setAddress("123 Test St");
		testUser.setRole(Role.product_manager);

		Category category = new Category();
		category.setName("Toys");
		category.setDescription("Kids' toys");
		Optional<Category> savedCategory = categoryRepository.insertNewCategory(category);
		
		Product product = new Product();
		product.setName("Lego Set");
		product.setModel("Star Wars");
		product.setSerialNumber("LG-404");
		product.setDescription("Lego Star Wars set");
		product.setPrice(new BigDecimal("59.99"));
		product.setQuantityInStock(40);
		product.setCategory(savedCategory.get());
		product.setProductManager(testUser);
		Optional<Product> inserted = productRepository.insertNewProduct(product);
		
		productRepository.deleteProduct(inserted.get());
		Optional<Product> found = productRepository.findById(inserted.get().getId());
		Assertions.assertFalse(found.isPresent(), "Product should be deleted");
	}

	// Clears the test data for each repository after each test
	@AfterEach
    void cleanUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

	// Runs after all the tests are completed
	@AfterAll
	static void tearDown() {
		// Closes the mock PostgreSQL database container
		postgresContainer.close();
	}
}
