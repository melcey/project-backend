package com.cs308.backend.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.AuthResponse;
import com.cs308.backend.dto.LoginRequest;
import com.cs308.backend.dto.MessageResponse;
import com.cs308.backend.dto.SignUpRequest;
import com.cs308.backend.security.JwtTokenProvider;
import com.cs308.backend.service.UserService;

/*
 * How to retrieve the user details in a controller or service (later use)?
 * Include the token as a header:
 *      Authorization: Bearer <your-token>
 * JwtAuthenticationFilter will handle the filtering
 * Include the following lines in the controller or service:
 *      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
 *      UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    public AuthController(AuthenticationManager authenticationManager,
            UserService userService,
            JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
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
        if (userService.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email already in use!"));
        }

        try {
            // Create new user
            User user = new User(
                signUpRequest.getName(),
                signUpRequest.getAddress(),
                Role.fromString(signUpRequest.getRole()));

            // Save user with encrypted email and password
            Optional<User> createdUser = userService.insertNewUser(
                user,
                signUpRequest.getEmail(),
                signUpRequest.getPassword());

            if (!(createdUser.isPresent())) {
                return ResponseEntity.badRequest().body(new MessageResponse("User creation failed"));
            }

            // No authentication will be performed while registering a user; they will need to explicitly log in

            // Return the success message
            return ResponseEntity.ok(new MessageResponse("OK"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("User creation failed"));
        }
    }
}
