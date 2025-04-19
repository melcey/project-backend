package com.cs308.backend.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.service.UserService;

public class CustomAuthenticationProviderTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private CustomAuthenticationProvider authenticationProvider;

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
    void testAuthenticate_ValidCredentials() {
        // Mock the UserService to return the test user
        when(userService.findByEmailAndPasswordAndRole("johndoe@example.com", "password123", "customer"))
                .thenReturn(java.util.Optional.of(testUser));

        // Create an authentication token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "johndoe@example.com", "password123", null);

        // Call the authenticate method
        Authentication result = authenticationProvider.authenticate(authentication);

        // Assertions
        assertThat(result).isNotNull();
        assertThat(result.isAuthenticated()).isTrue();
        assertThat(result.getPrincipal()).isInstanceOf(UserPrincipal.class);
        UserPrincipal userPrincipal = (UserPrincipal) result.getPrincipal();
        assertThat(userPrincipal.getUser()).isEqualTo(testUser);

        // Verify interactions with the UserService
        verify(userService, times(1)).findByEmailAndPasswordAndRole("johndoe@example.com", "password123", "customer");
    }

    @Test
    void testAuthenticate_InvalidCredentials() {
        // Mock the UserService to return an empty Optional
        when(userService.findByEmailAndPasswordAndRole("johndoe@example.com", "wrongpassword", "customer"))
                .thenReturn(java.util.Optional.empty());

        // Create an authentication token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "johndoe@example.com", "wrongpassword", null);

        // Call the authenticate method and expect an exception
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            authenticationProvider.authenticate(authentication);
        });

        // Assertions
        assertThat(exception.getMessage()).isEqualTo("Invalid email/password combination or role");

        // Verify interactions with the UserService
        verify(userService, times(1)).findByEmailAndPasswordAndRole("johndoe@example.com", "wrongpassword", "customer");
    }

    @Test
    void testSupports_ValidClass() {
        // Verify that the provider supports UsernamePasswordAuthenticationToken
        boolean supports = authenticationProvider.supports(UsernamePasswordAuthenticationToken.class);
        assertThat(supports).isTrue();
    }

    @Test
    void testSupports_InvalidClass() {
        // Verify that the provider does not support other classes
        boolean supports = authenticationProvider.supports(String.class);
        assertThat(supports).isFalse();
    }
}