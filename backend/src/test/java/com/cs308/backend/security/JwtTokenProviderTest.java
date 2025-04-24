package com.cs308.backend.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.security.Key;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;

import io.jsonwebtoken.security.Keys;

public class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    private String jwtSecret = "testSecretKeyForJwtTokenProvider1234567890"; // Mock secret key
    private int jwtExpirationInMs = 3600000; // 1 hour expiration

    @Mock
    private UserPrincipal mockUserPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.setJwtSecret(jwtSecret);
        jwtTokenProvider.setJwtExpirationInMs(jwtExpirationInMs);

        // Initialize the key
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        jwtTokenProvider.setKey(key);
    }

    @Test
    void testGenerateToken() {
        // Mock UserPrincipal and Authentication
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Role.customer); // Mock role
        when(mockUserPrincipal.getUser()).thenReturn(mockUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUserPrincipal, null);

        // Generate token
        String token = jwtTokenProvider.generateToken(authentication);

        // Validate token
        assertThat(token).isNotNull();
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        assertThat(userId).isEqualTo(1L);

        // Validate role claim
        String role = jwtTokenProvider.getRoleFromToken(token);
        assertThat(role).isEqualTo("ROLE_CUSTOMER");
    }

    @Test
    void testGetUserIdFromToken() {
        // Mock UserPrincipal and Authentication
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Role.customer); // Mock role
        when(mockUserPrincipal.getUser()).thenReturn(mockUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUserPrincipal, null);

        // Generate token
        String token = jwtTokenProvider.generateToken(authentication);

        // Extract user ID from token
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void testValidateToken_ValidToken() {
        // Mock UserPrincipal and Authentication
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Role.customer); // Mock role
        when(mockUserPrincipal.getUser()).thenReturn(mockUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUserPrincipal, null);

        // Generate token
        String token = jwtTokenProvider.generateToken(authentication);

        // Validate token
        boolean isValid = jwtTokenProvider.validateToken(token);
        assertThat(isValid).isTrue();
    }

    @Test
    void testValidateToken_ExpiredToken() throws InterruptedException {
        // Mock UserPrincipal and Authentication
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Role.customer); // Mock role
        when(mockUserPrincipal.getUser()).thenReturn(mockUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUserPrincipal, null);

        // Set a very short expiration time
        jwtTokenProvider.setJwtExpirationInMs(1); // 1 millisecond
        String token = jwtTokenProvider.generateToken(authentication);

        // Wait for the token to expire
        Thread.sleep(10);

        // Validate token
        boolean isValid = jwtTokenProvider.validateToken(token);
        assertThat(isValid).isFalse();
    }

    @Test
    void testValidateToken_InvalidToken() {
        // Validate an invalid token
        String invalidToken = "invalid.token.value";
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);
        assertThat(isValid).isFalse();
    }
}