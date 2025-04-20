package com.cs308.backend.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
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
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;

@DataJpaTest
@Testcontainers
public class ProductRepositoryTest {
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
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product testProduct;
    private User productManager;
    private Category category;

    @BeforeEach
    void setUp() {
        // Create and save a User entity
        productManager = new User();
        productManager.setName("John Doe");
        productManager.setRole(Role.product_manager);
        userRepository.save(productManager);

        // Create and save a Category entity
        category = new Category();
        category.setName("Electronics");
        category.setDescription("Electronic devices and gadgets.");
        categoryRepository.save(category);

        // Create and save a Product entity
        testProduct = new Product();
        testProduct.setName("Smartphone");
        testProduct.setModel("Galaxy S21");
        testProduct.setSerialNumber("SN123456");
        testProduct.setDescription("A high-end smartphone.");
        testProduct.setQuantityInStock(50);
        testProduct.setPrice(BigDecimal.valueOf(999.99));
        testProduct.setWarrantyStatus("1 Year");
        testProduct.setDistributorInfo("Samsung");
        testProduct.setIsActive(true);
        testProduct.setCategory(category);
        testProduct.setProductManager(productManager);
        productRepository.save(testProduct);
    }

    @Test
    void testFindByName() {
        List<Product> products = productRepository.findByName("Smartphone");
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getName()).isEqualTo("Smartphone");
    }

    @Test
    void testFindByCategoryId() {
        List<Product> products = productRepository.findByCategoryId(category.getId());
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getCategory().getName()).isEqualTo("Electronics");
    }

    @Test
    void testFindByProductManagerId() {
        List<Product> products = productRepository.findByProductManagerId(productManager.getId());
        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getProductManager().getName()).isEqualTo("John Doe");
    }

    @Test
    void testInsertNewProduct() {
        Product newProduct = new Product();
        newProduct.setName("Laptop");
        newProduct.setModel("XPS 13");
        newProduct.setSerialNumber("SN654321");
        newProduct.setDescription("A high-performance laptop.");
        newProduct.setQuantityInStock(30);
        newProduct.setPrice(BigDecimal.valueOf(1299.99));
        newProduct.setWarrantyStatus("2 Years");
        newProduct.setDistributorInfo("Dell");
        newProduct.setIsActive(true);
        newProduct.setCategory(category);
        newProduct.setProductManager(productManager);

        Optional<Product> savedProduct = productRepository.insertNewProduct(newProduct);
        assertThat(savedProduct).isPresent();
        assertThat(savedProduct.get().getName()).isEqualTo("Laptop");
    }

    @Test
    void testDeleteProduct() {
        productRepository.deleteProduct(testProduct);
        Optional<Product> deletedProduct = productRepository.findById(testProduct.getId());
        assertThat(deletedProduct).isNotPresent();
    }

    @Test
    void testUpdateProductName() {
        Optional<Product> updatedProduct = productRepository.updateProductName(testProduct, "Updated Smartphone");
        assertThat(updatedProduct).isPresent();
        assertThat(updatedProduct.get().getName()).isEqualTo("Updated Smartphone");
    }

    @Test
    void testUpdateProductPrice() {
        Optional<Product> updatedProduct = productRepository.updateProductPrice(testProduct, BigDecimal.valueOf(899.99));
        assertThat(updatedProduct).isPresent();
        assertThat(updatedProduct.get().getPrice()).isEqualTo(BigDecimal.valueOf(899.99));
    }

    @Test
    void testUpdateProductCategory() {
        Category newCategory = new Category();
        newCategory.setName("Home Appliances");
        newCategory.setDescription("Appliances for home use.");
        categoryRepository.save(newCategory);

        Optional<Product> updatedProduct = productRepository.updateProductCategory(testProduct, newCategory);
        assertThat(updatedProduct).isPresent();
        assertThat(updatedProduct.get().getCategory().getName()).isEqualTo("Home Appliances");
    }

    @AfterEach
    void cleanUp() {
        productRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }

	@AfterAll
	static void tearDown() {
		postgresContainer.close();
	}
}