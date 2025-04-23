package com.cs308.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.LoginRequest;
import com.cs308.backend.dto.SignUpRequest;
import com.cs308.backend.security.JwtTokenProvider;
import com.cs308.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
public class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAuthenticateUser_Success() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password", "customer");
        Authentication authentication = mock(Authentication.class);
        String jwtToken = "mockJwtToken";

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword());
        authToken.setDetails(loginRequest.getRole());

        when(authenticationManager.authenticate(authToken)).thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn(jwtToken);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value(jwtToken));

        // Verify that the authenticate method was called once
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAuthenticateUser_Failure() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongpassword", "customer");

        // Mock the behavior of the AuthenticationManager to throw an exception
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new RuntimeException("Authentication failed"));
    
        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());
    
        // Verify that the authenticate method was called once
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest("John Doe", "test@example.com", "password", "123 Street", "customer");
        User expectedUser = new User("John Doe", "123 Street", Role.customer);

        when(userService.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());
        when(userService.insertNewUser(any(User.class), eq(signUpRequest.getEmail()), eq(signUpRequest.getPassword())))
                .thenReturn(Optional.of(expectedUser)); // Simulate success by returning a valid User

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(signUpRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("OK"));

        // Verify method calls
        verify(userService, times(1)).findByEmail(signUpRequest.getEmail());
        verify(userService, times(1)).insertNewUser(any(User.class), eq(signUpRequest.getEmail()), eq(signUpRequest.getPassword()));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() throws Exception {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest("John Doe", "test@example.com", "password", "123 Street", "customer");

        when(userService.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Email already in use!"));

        // Verify that the findByEmail method was called once
        verify(userService, times(1)).findByEmail(signUpRequest.getEmail());
    }

    @Test
    void testRegisterUser_Failure() throws Exception {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest("John Doe", "test@example.com", "password", "123 Street", "customer");

        when(userService.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());
        when(userService.insertNewUser(any(User.class), eq(signUpRequest.getEmail()), eq(signUpRequest.getPassword())))
            .thenReturn(Optional.empty()); // Simulate failure by returning an empty Optional

        // Act & Assert
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("User registration failed"));

        // Verify method calls
        verify(userService, times(1)).findByEmail(signUpRequest.getEmail());
        verify(userService, times(1)).insertNewUser(any(User.class), eq(signUpRequest.getEmail()), eq(signUpRequest.getPassword()));
    }
}