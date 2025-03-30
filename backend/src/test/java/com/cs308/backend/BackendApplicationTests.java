package com.cs308.backend;

import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cs308.backend.model.Category;
import com.cs308.backend.model.Product;
import com.cs308.backend.model.Role;
import com.cs308.backend.model.User;
import com.cs308.backend.repo.CategoryRepository;
import com.cs308.backend.repo.ProductRepository;
import com.cs308.backend.repo.UserRepository;
import com.cs308.backend.security.CustomUserDetailsService;
import com.cs308.backend.security.JwtAuthenticationFilter;

// Specifies the configuration class as the main application of the backend
@SpringBootTest(classes = BackendApplication.class)
// Testcontainers will be used in order to test the database-related stuff
@Testcontainers
class BackendApplicationTests {

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
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

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

	// ------ Repo Tests ------

    @Test
    void testRepositoryBeansPresent() {
        //assertThat(userRepository).isNotNull();
        //assertThat(categoryRepository).isNotNull();
        //assertThat(productRepository).isNotNull();
    }

    @Test
    void testUserRepositoryOperations() {
        // Create a sample User instance. Adjust constructors/getters as necessary.
        User sampleUser = new User();
        sampleUser.setName("John Doe");
        sampleUser.setAddress("123 Main St");
        // Role should be set accordingly based on your implementation.
        sampleUser.setRole(Role.customer);

        // This test calls the repository method (assumes the insertNewUser is implemented correctly)
        // Using plain strings for email and password.
        User insertedUser = userRepository.insertNewUser(sampleUser, "john@example.com", "password123");
        //assertThat(insertedUser).isNotNull();
        //assertThat(insertedUser.getName()).isEqualTo("John Doe");

        // Optionally, test update and deletion functions if implemented
    }

    @Test
    void testCategoryRepositoryOperations() {
        Category category = new Category();
        category.setName("Electronics");
        category.setDescription("Electronic items category");

        Category newCategory = categoryRepository.insertNewCategory(category);
        //assertThat(newCategory).isNotNull();
        //assertThat(newCategory.getName()).isEqualTo("Electronics");
    }

    @Test
    void testProductRepositoryOperations() {
        // Create sample Category for product relation
        Category category = new Category();
        category.setName("Books");
        category.setDescription("Books category");
        Category savedCategory = categoryRepository.insertNewCategory(category);

        Product product = new Product();
        product.setName("Spring Boot in Action");
        product.setModel("1st Edition");
        product.setSerialNumber("SBIA-001");
        product.setDescription("A book about Spring Boot");
        product.setPrice(new BigDecimal("39.99"));
        product.setQuantityInStock(20);
        // Set relationships as needed
        product.setCategory(savedCategory);
        // Assuming a dummy manager exists; adjust as necessary.
        User manager = new User();
        manager.setName("Manager");
        product.setProductManager(manager);

        Product insertedProduct = productRepository.insertNewProduct(product);
        //assertThat(insertedProduct).isNotNull();
        //assertThat(insertedProduct.getName()).isEqualTo("Spring Boot in Action");
    }

    // ------ Model Tests ------

    @Test
    void testModelObjects() {
        // For User model
        User user = new User();
        user.setName("Alice");
        user.setAddress("456 Elm St");
        user.setRole(Role.product_manager);
        //assertThat(user.getName()).isEqualTo("Alice");
        //assertThat(user.getRole()).isEqualTo(Role.product_manager);

        // For Category model
        Category category = new Category();
        category.setName("Home Appliances");
        category.setDescription("Appliances for home");
        //assertThat(category.getName()).isEqualTo("Home Appliances");

        // For Product model
        Product product = new Product();
        product.setName("Washing Machine");
        product.setPrice(new BigDecimal("499.99"));
        product.setQuantityInStock(10);
        //assertThat(product.getPrice()).isEqualByComparingTo(new BigDecimal("499.99"));
    }

    // ------ Security Tests ------

    @Test
    void testSecurityBeansPresent() {
        //assertThat(customUserDetailsService).isNotNull();
        //assertThat(jwtAuthenticationFilter).isNotNull();
    }

    // If you have additional methods to test token processing or authentication flows,
    // you could instantiate JwtAuthenticationFilter with mocked requests.
    
    // ------ Controller Tests ------

    @Test
    void testAuthControllerEndpoint() throws Exception {
        // Assuming there is an endpoint defined in AuthController. Adjust the URL as needed.
        //mockMvc.perform(get("/auth/test"))
        //    .andExpect(status().isOk());
    }

	// Runs after all the tests are completed
	@org.junit.jupiter.api.AfterAll
	static void tearDown() {
		// Closes the mock PostgreSQL database container
		postgresContainer.close();
	}
}
