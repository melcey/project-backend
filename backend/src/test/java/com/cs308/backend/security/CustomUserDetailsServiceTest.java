package com.cs308.backend.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.UserRepository;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a test User
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEncryptedEmail("johndoe@example.com".getBytes());
        testUser.setPasswordHashed("password123".getBytes());
        testUser.setRole(Role.customer);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Mock the repository to return the test user
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(Optional.of(testUser));

        // Call the service method
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("johndoe@example.com");

        // Assertions
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("johndoe@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("password123");
        verify(userRepository, times(1)).findByEmail("johndoe@example.com");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Mock the repository to return an empty Optional
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // Call the service method and expect an exception
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("unknown@example.com");
        });

        // Assertions
        assertThat(exception.getMessage()).isEqualTo("User not found with email: unknown@example.com");
        verify(userRepository, times(1)).findByEmail("unknown@example.com");
    }

    @Test
    void testLoadUserById_UserFound() {
        // Mock the repository to return the test user
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Call the service method
        UserDetails userDetails = customUserDetailsService.loadUserById(1L);

        // Assertions
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("johndoe@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("password123");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testLoadUserById_UserNotFound() {
        // Mock the repository to return an empty Optional
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the service method and expect an exception
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserById(2L);
        });

        // Assertions
        assertThat(exception.getMessage()).isEqualTo("User not found with id: 2");
        verify(userRepository, times(1)).findById(2L);
    }
}