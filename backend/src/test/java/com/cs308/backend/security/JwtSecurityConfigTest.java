package com.cs308.backend.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import com.cs308.backend.service.UserService;

public class JwtSecurityConfigTest {

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private JwtSecurityConfig jwtSecurityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testJwtAuthenticationFilter() {
        JwtAuthenticationFilter filter = jwtSecurityConfig.jwtAuthenticationFilter();
        assertThat(filter).isNotNull();
    }

    @Test
    void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = jwtSecurityConfig.passwordEncoder();
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder.encode("password")).isNotEmpty();
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);

        AuthenticationManager result = jwtSecurityConfig.authenticationManager(authenticationConfiguration);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(authenticationManager);
        verify(authenticationConfiguration, times(1)).getAuthenticationManager();
    }

    @Test
    void testAuthenticationProvider() {
        AuthenticationProvider authenticationProvider = jwtSecurityConfig.authenticationProvider(userService);
        assertThat(authenticationProvider).isNotNull();
        assertThat(authenticationProvider).isInstanceOf(CustomAuthenticationProvider.class);
    }

    @Test
    void testSecurityFilterChain() throws Exception {
        // Mock HttpSecurity
        HttpSecurity httpSecurity = mock(HttpSecurity.class);

        // Mock chained method calls
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.authenticationProvider(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);

        // Mock the build() method
        DefaultSecurityFilterChain mockFilterChain = mock(DefaultSecurityFilterChain.class);
        when(httpSecurity.build()).thenReturn(mockFilterChain);

        // Call the filterChain method
        SecurityFilterChain filterChain = jwtSecurityConfig.filterChain(httpSecurity, mock(AuthenticationProvider.class));

        // Assertions
        assertThat(filterChain).isNotNull();
        assertThat(filterChain).isEqualTo(mockFilterChain);
    }
}