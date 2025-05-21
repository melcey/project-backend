package com.cs308.backend.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Rating;
import com.cs308.backend.dao.User;
import com.cs308.backend.dao.Role;

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

@DataJpaTest
@Testcontainers
public class RatingRepositoryTest {
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
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testFindById() {
        // Create a User
        User user = new User("John Doe", "123 Main St", Role.customer, "taxId");
        User savedUser = userRepository.save(user);

        // Create a Product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(1999.99));
        Product savedProduct = productRepository.save(product);

        // Create a Rating
        Rating rating = new Rating();
        rating.setRating(5);
        rating.setRatingDate(LocalDateTime.now());
        rating.setRatedProduct(savedProduct);
        rating.setRatingUser(savedUser);
        Rating savedRating = ratingRepository.save(rating);

        // Fetch the Rating by ID
        Optional<Rating> fetchedRating = ratingRepository.findById(savedRating.getId());

        // Assert
        assertTrue(fetchedRating.isPresent());
        assertEquals(5, fetchedRating.get().getRating());
    }

    @Test
    public void testFindByRatedProduct() {
        // Create a User
        User user = new User("John Doe", "123 Main St", Role.customer, "taxId");
        User savedUser = userRepository.save(user);

        // Create a Product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(1999.99));
        Product savedProduct = productRepository.save(product);

        // Create a Rating
        Rating rating = new Rating();
        rating.setRatedProduct(savedProduct);
        rating.setRating(4);
        rating.setRatingDate(LocalDateTime.now());
        rating.setRatingUser(savedUser);
        ratingRepository.save(rating);

        // Fetch Ratings by Product
        List<Rating> ratings = ratingRepository.findByRatedProduct(savedProduct);

        // Assert
        assertEquals(1, ratings.size());
        assertEquals(4, ratings.get(0).getRating());
    }

    @Test
    public void testFindByRatingUser() {
        // Create a User
        User user = new User("John Doe", "123 Main St", Role.customer, "taxId");
        User savedUser = userRepository.save(user);

        // Create a Product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(1999.99));
        Product savedProduct = productRepository.save(product);

        // Create a Rating
        Rating rating = new Rating();
        rating.setRatingUser(savedUser);
        rating.setRating(3);
        rating.setRatingDate(LocalDateTime.now());
        rating.setRatedProduct(savedProduct);
        ratingRepository.save(rating);

        // Fetch Ratings by User
        List<Rating> ratings = ratingRepository.findByRatingUser(savedUser);

        // Assert
        assertEquals(1, ratings.size());
        assertEquals(3, ratings.get(0).getRating());
    }

    @Test
    public void testFindByRatingRange() {
        // Create a User
        User user = new User("John Doe", "123 Main St", Role.customer, "taxId");
        User savedUser = userRepository.save(user);

        // Create a Product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(1999.99));
        Product savedProduct = productRepository.save(product);

        // Create Ratings
        Rating rating1 = new Rating();
        rating1.setRating(2);
        rating1.setRatingDate(LocalDateTime.now());
        rating1.setRatingUser(savedUser);
        rating1.setRatedProduct(savedProduct);
        ratingRepository.save(rating1);

        Rating rating2 = new Rating();
        rating2.setRating(4);
        rating2.setRatingDate(LocalDateTime.now());
        rating2.setRatingUser(savedUser);
        rating2.setRatedProduct(savedProduct);
        ratingRepository.save(rating2);

        // Fetch Ratings by range
        List<Rating> ratings = ratingRepository.findByRatingGreaterThanEqual(3);

        // Assert
        assertEquals(1, ratings.size());
        assertEquals(4, ratings.get(0).getRating());
    }

    @Test
    public void testFindByRatingDateBetween() {
        // Create a User
        User user = new User("John Doe", "123 Main St", Role.customer, "taxId");
        User savedUser = userRepository.save(user);

        // Create a Product
        Product product = new Product();
        product.setName("Sample Product");
        product.setPrice(BigDecimal.valueOf(1999.99));
        Product savedProduct = productRepository.save(product);

        // Create Ratings
        Rating rating1 = new Rating();
        rating1.setRating(5);
        rating1.setRatingDate(LocalDateTime.now().minusDays(2));
        rating1.setRatedProduct(savedProduct);
        rating1.setRatingUser(savedUser);
        ratingRepository.save(rating1);

        Rating rating2 = new Rating();
        rating2.setRating(3);
        rating2.setRatingDate(LocalDateTime.now().minusDays(1));
        rating2.setRatedProduct(savedProduct);
        rating2.setRatingUser(savedUser);
        ratingRepository.save(rating2);

        // Fetch Ratings by date range
        List<Rating> ratings = ratingRepository.findByRatingDateBetween(
            LocalDateTime.now().minusDays(3), LocalDateTime.now());

        // Assert
        assertEquals(2, ratings.size());
    }

    @Test
    public void testFindByProductAndRating() {
        // Create a User
        User user = new User("John Doe", "123 Main St", Role.customer, "taxId");
        User savedUser = userRepository.save(user);

        // Create a Product
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(1999.99));
        Product savedProduct = productRepository.save(product);

        // Create Ratings
        Rating rating1 = new Rating();
        rating1.setRatedProduct(savedProduct);
        rating1.setRating(5);
        rating1.setRatingDate(LocalDateTime.now());
        rating1.setRatedProduct(savedProduct);
        rating1.setRatingUser(savedUser);
        ratingRepository.save(rating1);

        Rating rating2 = new Rating();
        rating2.setRatedProduct(savedProduct);
        rating2.setRating(3);
        rating2.setRatingDate(LocalDateTime.now());
        rating2.setRatedProduct(savedProduct);
        rating2.setRatingUser(savedUser);
        ratingRepository.save(rating2);

        // Fetch Ratings by Product and Rating
        List<Rating> ratings = ratingRepository.findByRatedProductAndRating(savedProduct, 5);

        // Assert
        assertEquals(1, ratings.size());
        assertEquals(5, ratings.get(0).getRating());
    }

    @AfterEach
    void cleanUp() {
        ratingRepository.deleteAll();
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

	@AfterAll
	static void tearDown() {
		postgresContainer.close();
	}
}