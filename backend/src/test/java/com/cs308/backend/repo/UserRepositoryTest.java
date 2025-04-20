package com.cs308.backend.repo;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;

@DataJpaTest
@Testcontainers
public class UserRepositoryTest {
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
    private UserRepository userRepository;

    private User testUser;
    byte[] encryptedEmail;
    byte[] passwordHashed;

    @BeforeEach
    void setUp() {
        encryptedEmail = TestEncryptionUtil.encrypt("johndoe@example.com");
        passwordHashed = TestEncryptionUtil.encrypt("password123");

        // Create and save a User entity
        testUser = new User();
        testUser.setName("John Doe");
        testUser.setRole(Role.customer);
        testUser.setAddress("123 Main St");
        testUser.setEncryptedEmail(encryptedEmail);
        testUser.setPasswordHashed(passwordHashed);
        userRepository.save(testUser);
    }

    @Test
    void testFindByEmail() {
        Optional<User> user = userRepository.findByEmail("johndoe@example.com");
        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo("John Doe");
    }

    @Test
    void testFindByName() {
        List<User> users = userRepository.findByName("John Doe");
        assertThat(users).isNotEmpty();
        
        // Verify the encrypted email matches the plain text email
        boolean matches = TestEncryptionUtil.verify("johndoe@example.com", users.get(0).getEncryptedEmail());
        assertThat(matches).isTrue();
    }

    @Test
    void testFindByRole() {
        List<User> users = userRepository.findByRole(Role.customer);
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getRole()).isEqualTo(Role.customer);
    }

    @Test
    void testInsertNewUser() {
        User newUser = new User();
        newUser.setName("Jane Doe");
        newUser.setRole(Role.sales_manager);
        newUser.setAddress("456 Elm St");

        Optional<User> savedUser = userRepository.insertNewUser(newUser, "janedoe@example.com", "securepassword");
        assertThat(savedUser).isPresent();
        
        // Verify the encrypted email matches the plain text email
        boolean matches = TestEncryptionUtil.verify("janedoe@example.com", savedUser.get().getEncryptedEmail());
        assertThat(matches).isTrue();    
    }

    @Test
    void testDeleteUserById() {
        userRepository.deleteUserById(testUser);
        Optional<User> deletedUser = userRepository.findById(1L);
        assertThat(deletedUser).isNotPresent();
    }

    @Test
    void testUpdateUserName() {
        Optional<User> updatedUser = userRepository.updateUserName(testUser, "John Smith");
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getName()).isEqualTo("John Smith");
    }

    @Test
    void testUpdateUserEmail() {
        Optional<User> updatedUser = userRepository.updateUserEmail(testUser, "johnsmith@example.com");
        assertThat(updatedUser).isPresent();

        // Verify the encrypted email matches the updated plain text email
        boolean matches = TestEncryptionUtil.verify("johnsmith@example.com", updatedUser.get().getEncryptedEmail());
        assertThat(matches).isTrue();
    }

    @Test
    void testUpdateUserAddress() {
        Optional<User> updatedUser = userRepository.updateUserAddress(testUser, "789 Oak St");
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getAddress()).isEqualTo("789 Oak St");
    }

    @Test
    void testUpdateUserPassword() {
        Optional<User> updatedUser = userRepository.updateUserPassword(testUser, "newsecurepassword");
        assertThat(updatedUser).isPresent();
        
        // Verify the encrypted password matches the updated plain text password
        boolean matches = TestEncryptionUtil.verify("newsecurepassword", updatedUser.get().getPasswordHashed());
        assertThat(matches).isTrue();
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

	@AfterAll
	static void tearDown() {
		postgresContainer.close();
	}

    class TestEncryptionUtil {
        /**
         * Encrypts a value using BCrypt, similar to PostgreSQL's `crypt()` function.
         *
         * @param value The plain text value to encrypt.
         * @return The encrypted value as a byte[].
         */
        public static byte[] encrypt(String value) {
            String hashedValue = BCrypt.hashpw(value, BCrypt.gensalt());
            return hashedValue.getBytes(StandardCharsets.UTF_8); // Convert to byte[]
        }
    
        /**
         * Verifies a plain text value against an encrypted value.
         *
         * @param plainValue The plain text value.
         * @param encryptedValue The encrypted value to compare against (as byte[]).
         * @return True if the plain value matches the encrypted value, false otherwise.
         */
        public static boolean verify(String plainValue, byte[] encryptedValue) {
            String encryptedValueAsString = new String(encryptedValue, StandardCharsets.UTF_8); // Convert byte[] to String
            return BCrypt.checkpw(plainValue, encryptedValueAsString);
        }
    }
}