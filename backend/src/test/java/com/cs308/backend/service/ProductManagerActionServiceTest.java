package com.cs308.backend.service;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.ProductManagerActionRepository;

public class ProductManagerActionServiceTest {

    @Mock
    private ProductManagerActionRepository repository;

    @InjectMocks
    private ProductManagerActionService service;

    private User testProductManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a test product manager
        testProductManager = new User();
        testProductManager.setId(1L);
        testProductManager.setName("John Doe");
        testProductManager.setEncryptedEmail("johndoe@example.com".getBytes());
        testProductManager.setPasswordHashed("password123".getBytes());
        testProductManager.setRole(Role.product_manager);
        testProductManager.setAddress("123 Main St");
    }

    @Test
    void testLogAction() {
        // Arrange
        String actionType = "ADD_PRODUCT";
        String details = "Added a new product to the catalog.";

        // Act
        service.logAction(testProductManager, actionType, details);

        // Assert
        verify(repository, times(1)).save(argThat(action -> {
            // Verify the ProductManagerAction object passed to the repository
            assert action.getProductManager().equals(testProductManager);
            assert action.getActionType().equals(actionType);
            assert action.getDetails().equals(details);
            assert action.getActionDate() != null; // Ensure actionDate is set
            return true;
        }));
    }
}