package com.cs308.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.AuthResponse;
import com.cs308.backend.dto.LoginRequest;
import com.cs308.backend.dto.MessageResponse;
import com.cs308.backend.dto.SignUpRequest;
import com.cs308.backend.security.JwtTokenProvider;
import com.cs308.backend.service.UserService;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_Success() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password", "customer");
        Authentication authentication = mock(Authentication.class);
        String jwtToken = "mockJwtToken";

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword());
        authToken.setDetails(loginRequest.getRole());

        when(authenticationManager.authenticate(authToken)).thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn(jwtToken);

        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isInstanceOf(AuthResponse.class);
        assertThat(((AuthResponse) response.getBody()).getAccessToken()).isEqualTo(jwtToken);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> captor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager, times(1)).authenticate(captor.capture());
        assertThat(captor.getValue().getPrincipal()).isEqualTo(loginRequest.getEmail());
        assertThat(captor.getValue().getDetails()).isEqualTo(loginRequest.getRole());
    }

    @Test
    void testAuthenticateUser_Failure() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongpassword", "customer");
    
        // Mock the behavior of the AuthenticationManager to throw an exception
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new RuntimeException("Authentication failed"));
    
        // Act
        try {
            authController.authenticateUser(loginRequest);
        } catch (RuntimeException e) {
            // Assert
            assertThat(e.getMessage()).isEqualTo("Authentication failed");
        }
    
        // Verify that the authenticate method was called once
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest("John Doe", "test@example.com", "password", "123 Street", "customer");
        User expectedUser = new User("John Doe", "123 Street", Role.customer);

        when(userService.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());
        when(userService.insertNewUser(any(User.class), eq(signUpRequest.getEmail()), eq(signUpRequest.getPassword())))
                .thenReturn(Optional.of(expectedUser)); // Simulate success by returning a valid User

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(response.getBody()).isEqualTo(new MessageResponse("OK"));

        // Capture the User object passed to insertNewUser
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).insertNewUser(userCaptor.capture(), eq(signUpRequest.getEmail()), eq(signUpRequest.getPassword()));

        // Verify the captured User object
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getName()).isEqualTo("John Doe");
        assertThat(capturedUser.getAddress()).isEqualTo("123 Street");
        assertThat(capturedUser.getRole()).isEqualTo(Role.customer);

        // Verify other method calls
        verify(userService, times(1)).findByEmail(signUpRequest.getEmail());
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest("John Doe", "test@example.com", "password",  "123 Street", "customer");

        when(userService.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.of(new User()));

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(new MessageResponse("Email already in use!"));
        verify(userService, times(1)).findByEmail(signUpRequest.getEmail());
    }

    @Test
    void testRegisterUser_Failure() {
        // Arrange
        SignUpRequest signUpRequest = new SignUpRequest("John Doe", "test@example.com", "password", "123 Street", "customer");
        User user = new User("John Doe", "123 Street", Role.customer);

        when(userService.findByEmail(signUpRequest.getEmail())).thenReturn(Optional.empty());
        when(userService.insertNewUser(any(User.class), eq(signUpRequest.getEmail()), eq(signUpRequest.getPassword())))
                .thenReturn(Optional.empty()); // Simulate failure by returning Optional.empty()

        // Act
        ResponseEntity<?> response = authController.registerUser(signUpRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isInstanceOf(MessageResponse.class);
        assertThat(response.getBody()).isEqualTo(new MessageResponse("User creation failed"));

        verify(userService, times(1)).findByEmail(signUpRequest.getEmail());

        verify(userService, times(1)).insertNewUser(user, signUpRequest.getEmail(), signUpRequest.getPassword());
    }
}