package com.cs308.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.LoginRequest;
import com.cs308.backend.dto.SignUpRequest;
import com.cs308.backend.repo.UserRepository;
import com.cs308.backend.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
// Spring Security will not block the endpoints from being called during the tests
@WithMockUser
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    // Mocks required by AuthController
    @MockitoBean
    private AuthenticationManager authenticationManager;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private JwtTokenProvider tokenProvider;

    // 2025-03-30 20:56:40 [ERROR]   AuthControllerTests.testAuthenticateUser_Success » IllegalState ApplicationContext failure threshold (1) exceeded
    @Test
    void testAuthenticateUser_Success() throws Exception {
        // Prepare login request
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("test123");
        loginRequest.setRole(Role.product_manager);

        // Prepare an authentication object to be returned upon successful authentication.
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        // Simulate an authenticated object.
        authToken.setDetails(loginRequest.getRole());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authToken);

        // Simulate token generation
        when(tokenProvider.generateToken(any(Authentication.class))).thenReturn("dummy.jwt.token");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("dummy.jwt.token"));
    }

    //2025-03-30 20:56:40 [ERROR]   AuthControllerTests.testRegisterUser_Success » IllegalState ApplicationContext failure threshold (1) exceeded
    @Test
    void testRegisterUser_Success() throws Exception {
        // Prepare signUp request where user does not already exist.
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName("New User");
        signUpRequest.setEmail("newuser@example.com");
        signUpRequest.setPassword("newpass");
        signUpRequest.setAddress("456 New Ave");
        signUpRequest.setRole(Role.customer);

        // Simulate repository lookup returning empty so that user does not exist.
        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());

        // Simulate repository insertion by returning a dummy user with an id.
        User dummyUser = new User(signUpRequest.getName(), signUpRequest.getAddress(), signUpRequest.getRole());
        dummyUser.setId(1L);
        when(userRepository.insertNewUser(any(User.class), anyString(), anyString())).thenReturn(dummyUser);

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isOk())
            .andExpect(content().string("OK"));
    }

    //2025-03-30 20:56:40 [ERROR]   AuthControllerTests.testRegisterUser_EmailExists » IllegalState Failed to load ApplicationContext for [WebMergedContextConfiguration@1dd76982 testClass = com.cs308.backend.controller.AuthControllerTests, locations = [], classes = [com.cs308.backend.BackendApplication]
    @Test
    void testRegisterUser_EmailExists() throws Exception {
        // Prepare signUp request where user already exists.
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setName("Existing User");
        signUpRequest.setEmail("existing@example.com");
        signUpRequest.setPassword("pass123");
        signUpRequest.setAddress("789 Existing Rd");
        signUpRequest.setRole(Role.customer);

        // Simulate repository lookup returning an existing user.
        User existingUser = new User(signUpRequest.getName(), signUpRequest.getAddress(), signUpRequest.getRole());
        existingUser.setId(2L);
        when(userRepository.findByEmail("existing@example.com")).thenReturn(java.util.Optional.of(existingUser));

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Email already in use!"));
    }
}