package com.cs308.backend.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

public class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        // Clear the SecurityContextHolder before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_ValidJwt() throws ServletException, IOException {
        // Arrange
        String jwt = "valid.jwt.token";
        Long userId = 1L;
        UserDetails userDetails = mock(UserDetails.class);

        request.addHeader("Authorization", String.format("Bearer %s", jwt));

        when(tokenProvider.validateToken(jwt)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(jwt)).thenReturn(userId);
        when(customUserDetailsService.loadUserById(userId)).thenReturn(userDetails);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        UsernamePasswordAuthenticationToken authentication = 
            (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);
        verify(filterChain, times(1)).doFilter(request, response);
        verify(tokenProvider, times(1)).validateToken(jwt);
        verify(tokenProvider, times(1)).getUserIdFromToken(jwt);
        verify(customUserDetailsService, times(1)).loadUserById(userId);
    }

    @Test
    void testDoFilterInternal_InvalidJwt() throws ServletException, IOException {
        // Arrange
        String jwt = "invalid.jwt.token";

        request.addHeader("Authorization", String.format("Bearer %s", jwt));

        when(tokenProvider.validateToken(jwt)).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_NoJwt() throws ServletException, IOException {
        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_SkipAuthForAuthEndpoint() throws ServletException, IOException {
        // Arrange
        request.setRequestURI("/auth/login");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_SkipAuthForProductsEndpoint() throws ServletException, IOException {
        // Arrange
        request.setRequestURI("/products/list");

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @AfterEach
    void tearDown() {
        // Clear the SecurityContextHolder after each test
        SecurityContextHolder.clearContext();
    }
}