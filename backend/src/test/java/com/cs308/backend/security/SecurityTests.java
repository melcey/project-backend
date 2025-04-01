package com.cs308.backend.security;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.UserRepository;
import com.cs308.backend.service.UserService;

import jakarta.servlet.FilterChain;

// To be revisited
class SecurityTests {

    private UserRepository userRepositoryMock;
    private UserService userServiceMock;
    private CustomAuthenticationProvider customAuthProvider;

    @BeforeEach
    void setup() {
        userRepositoryMock = mock(UserRepository.class);
        userServiceMock = mock(UserService.class);
        customAuthProvider = new CustomAuthenticationProvider(userServiceMock);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testCustomAuthenticationProvider_Success() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setAddress("123 Test St");
        user.setRole(Role.product_manager);
        user.setEncryptedEmail("encryptedTest@example.com".getBytes(StandardCharsets.UTF_8));
        user.setPasswordHashed("hashedPassword".getBytes(StandardCharsets.UTF_8));

        // Simulate repository returning the user
        when(userRepositoryMock.findByEmailAndPasswordAndRole(anyString(), anyString(), anyString()))
            .thenReturn(Optional.of(user));

        UsernamePasswordAuthenticationToken authRequest =
            new UsernamePasswordAuthenticationToken("test@example.com", "password123");
        authRequest.setDetails(Role.product_manager);

        Authentication resultingAuth = customAuthProvider.authenticate(authRequest);
        Assertions.assertNotNull(resultingAuth);
        Assertions.assertTrue(resultingAuth.isAuthenticated());

        UserPrincipal principal = (UserPrincipal) resultingAuth.getPrincipal();
        Assertions.assertEquals(user, principal.getUser());
    }

    @Test
    void testCustomAuthenticationProvider_Failure() {
        when(userRepositoryMock.findByEmailAndPasswordAndRole(anyString(), anyString(), anyString()))
            .thenReturn(Optional.empty());

        UsernamePasswordAuthenticationToken authRequest =
            new UsernamePasswordAuthenticationToken("invalid@example.com", "wrongpassword");
        authRequest.setDetails(Role.product_manager);

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            customAuthProvider.authenticate(authRequest);
        });
    }

    @Test
    void testCustomUserDetailsService_LoadUserByUsername_Success() {
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService(userRepositoryMock);

        User user = new User();
        user.setId(2L);
        user.setName("User Two");
        user.setEncryptedEmail("encrypted@example.com".getBytes(StandardCharsets.UTF_8));
        user.setPasswordHashed("hashed".getBytes(StandardCharsets.UTF_8));
        user.setRole(Role.customer);

        when(userRepositoryMock.findByEmail("encrypted@example.com")).thenReturn(Optional.of(user));

        // loadUserByUsername uses email as username
        org.springframework.security.core.userdetails.UserDetails userDetails =
            userDetailsService.loadUserByUsername("encrypted@example.com");
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(new String(user.getEncryptedEmail(), StandardCharsets.UTF_8), userDetails.getUsername());
    }

    @Test
    void testCustomUserDetailsService_LoadUserByUsername_NotFound() {
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService(userRepositoryMock);
        when(userRepositoryMock.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Assertions.assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@example.com");
        });
    }

    @Test
    void testCustomUserDetailsService_LoadUserById_Success() {
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService(userRepositoryMock);

        User user = new User();
        user.setId(3L);
        user.setName("User Three");
        user.setEncryptedEmail("enc3@example.com".getBytes(StandardCharsets.UTF_8));
        user.setPasswordHashed("hash3".getBytes(StandardCharsets.UTF_8));
        user.setRole(Role.sales_manager);

        when(userRepositoryMock.findById(3L)).thenReturn(Optional.of(user));

        org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserById(3L);
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(new String(user.getEncryptedEmail(), StandardCharsets.UTF_8), userDetails.getUsername());
    }

    @Test
    void testCustomUserDetailsService_LoadUserById_NotFound() {
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService(userRepositoryMock);
        when(userRepositoryMock.findById(999L)).thenReturn(Optional.empty());

        Assertions.assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserById(999L);
        });
    }

    @Test
    void testJwtTokenProvider_GenerateAndValidateToken() {
        JwtTokenProvider tokenProvider = new JwtTokenProvider();
        // Set fixed secret and expiration for testing
        tokenProvider.setJwtSecret("abcdefghijklmnopqrstuvwxzy012345"); // at least 32 chars
        tokenProvider.setJwtExpirationInMs(60000); // 1 minute
        tokenProvider.init(); // initialize key

        // Create a dummy Authentication with a UserPrincipal
        User user = new User();
        user.setId(100L);
        user.setEncryptedEmail("dummy@example.com".getBytes(StandardCharsets.UTF_8));
        user.setPasswordHashed("dummyHash".getBytes(StandardCharsets.UTF_8));
        user.setRole(Role.customer);
        UserPrincipal principal = UserPrincipal.create(user);

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        String token = tokenProvider.generateToken(auth);
        Assertions.assertNotNull(token);
        Assertions.assertTrue(tokenProvider.validateToken(token));

        Long extractedUserId = tokenProvider.getUserIdFromToken(token);
        Assertions.assertEquals(100L, extractedUserId);
    }

    @Test
    void testJwtTokenProvider_InvalidToken() {
        JwtTokenProvider tokenProvider = new JwtTokenProvider();
        tokenProvider.setJwtSecret("abcdefghijklmnopqrstuvwxzy012345");
        tokenProvider.setJwtExpirationInMs(60000);
        tokenProvider.init();

        String invalidToken = "invalid.token.value";
        Assertions.assertFalse(tokenProvider.validateToken(invalidToken));
    }

    @Test
    void testJwtAuthenticationFilter_SkipAuthForAuthEndpoint() throws Exception {
        // Setup mocks for token provider and user details service
        JwtTokenProvider tokenProvider = mock(JwtTokenProvider.class);
        when(tokenProvider.validateToken(anyString())).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(anyString())).thenReturn(50L);

        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        User user = new User();
        user.setId(50L);
        user.setEncryptedEmail("user50@example.com".getBytes(StandardCharsets.UTF_8));
        user.setPasswordHashed("hash50".getBytes(StandardCharsets.UTF_8));
        user.setRole(Role.customer);
        UserPrincipal principal = UserPrincipal.create(user);
        when(userDetailsService.loadUserById(50L)).thenReturn(principal);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(tokenProvider, userDetailsService);

        // Create a request with an /auth path that should bypass JWT processing
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/auth/login");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testJwtAuthenticationFilter_Authenticate() throws Exception {
        // Setup token provider to validate token and return user ID
        JwtTokenProvider tokenProvider = mock(JwtTokenProvider.class);
        when(tokenProvider.validateToken("valid.jwt.token")).thenReturn(true);
        when(tokenProvider.getUserIdFromToken("valid.jwt.token")).thenReturn(60L);

        // Setup user details service
        CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
        User user = new User();
        user.setId(60L);
        user.setEncryptedEmail("user60@example.com".getBytes(StandardCharsets.UTF_8));
        user.setPasswordHashed("hash60".getBytes(StandardCharsets.UTF_8));
        user.setRole(Role.customer);
        UserPrincipal principal = UserPrincipal.create(user);
        when(userDetailsService.loadUserById(60L)).thenReturn(principal);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(tokenProvider, userDetailsService);

        // Create a request with a valid JWT header
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/protected/resource");
        request.addHeader("Authorization", "Bearer valid.jwt.token");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, filterChain);

        Assertions.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        UserPrincipal authPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Assertions.assertEquals(60L, authPrincipal.getUser().getId());
        SecurityContextHolder.clearContext();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    // To be tested
    @Test
    void testJwtSecurityConfigBeans() {
        // Load Spring context for JwtSecurityConfig
        //2025-03-30 20:56:40 [ERROR]   SecurityTests.testJwtSecurityConfigBeans:241 » UnsatisfiedDependency Error creating bean with name 'jwtSecurityConfig': Unsatisfied dependency expressed through constructor parameter 0: No qualifying bean of type 'com.cs308.backend.security.CustomUserDetailsService' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JwtSecurityConfig.class);
        JwtAuthenticationFilter filter = context.getBean(JwtAuthenticationFilter.class);
        Assertions.assertNotNull(filter);
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        Assertions.assertNotNull(encoder);
        AuthenticationManager authManager = context.getBean(AuthenticationManager.class);
        Assertions.assertNotNull(authManager);
        context.close();
    }

    @Test
    void testUserPrincipalMethods() {
        User user = new User();
        user.setId(10L);
        user.setName("Principal User");
        user.setEncryptedEmail("principal@example.com".getBytes(StandardCharsets.UTF_8));
        user.setPasswordHashed("principalHash".getBytes(StandardCharsets.UTF_8));
        user.setRole(Role.sales_manager);

        UserPrincipal principal1 = UserPrincipal.create(user);
        Assertions.assertFalse(principal1.getAuthorities().isEmpty());
        Assertions.assertEquals("principal@example.com", principal1.getUsername());
        Assertions.assertEquals("principalHash", principal1.getPassword());

        UserPrincipal principal2 = UserPrincipal.create(user);
        Assertions.assertEquals(principal1, principal2);
        Assertions.assertEquals(principal1.hashCode(), principal2.hashCode());
    }
}