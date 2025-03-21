package com.cs308.backend;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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

	@Test
	// Tests whether the application context is loaded successfully.
	void contextLoads() {
	}

	@Test
	// Tests whether the application can connect to the database on the mock database container
	void testDatabaseConnection(@Autowired DataSource dataSource) throws SQLException {
		// Tries to connect to the mock database container
		try (Connection connection = dataSource.getConnection()) {
			// Checks whether the connection is valid or not, with a timeout of 2 seconds 
			Assertions.assertTrue(connection.isValid(2), "The connection should be valid");
		}
	}

	// Runs after all the tests are completed
	@org.junit.jupiter.api.AfterAll
	static void tearDown() {
		// Closes the mock PostgreSQL database container
		postgresContainer.close();
	}
}
