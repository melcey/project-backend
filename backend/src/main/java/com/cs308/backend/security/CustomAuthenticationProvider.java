package com.cs308.backend.security;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    public CustomAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        Role role = extractRoleFromAuthentication(authentication);

        // Find user with provided credentials
        User user = userRepository.findByEmailAndPasswordAndRole(email, password, role.getValue())
                .orElseThrow(() -> new BadCredentialsException("Invalid email/password combination or role"));

        // Create UserPrincipal from the found user
        UserPrincipal userPrincipal = UserPrincipal.create(user);

        // Create and return a new authenticated token
        return new UsernamePasswordAuthenticationToken(
                userPrincipal,
                null, // credentials are cleared after authentication
                userPrincipal.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Role extractRoleFromAuthentication(Authentication authentication) {
        // You might want to adjust this based on how you're passing the role
        // information
        // This is just an example - you could pass it as an additional parameter or in
        // a custom authentication token
        Object details = authentication.getDetails();
        if (details instanceof Role) {
            return (Role) details;
        }
        // Default to customer role if not specified
        return Role.customer;
    }
}
