package com.cs308.backend.controller;

import com.cs308.backend.dto.AuthResponse;
import com.cs308.backend.dto.LoginRequest;
import com.cs308.backend.dto.SignUpRequest;
import com.cs308.backend.model.User;
import com.cs308.backend.repo.UserRepository;
import com.cs308.backend.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Create authentication token with role
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword());
        // Set role in authentication details
        authToken.setDetails(loginRequest.getRole());

        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Set the authentication in context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = tokenProvider.generateToken(authentication);

        // Return the token
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        // Check if user already exists
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Email already in use!");
        }

        // Create new user
        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getAddress(),
                signUpRequest.getRole());

        // Save user with encrypted email and password
        user = userRepository.insertNewUser(
                user,
                signUpRequest.getEmail(),
                signUpRequest.getPassword());

        // Create authentication token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                signUpRequest.getEmail(),
                signUpRequest.getPassword());
        authToken.setDetails(signUpRequest.getRole());

        // Authenticate the new user
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Generate JWT token
        String jwt = tokenProvider.generateToken(authentication);

        // Return the token
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
