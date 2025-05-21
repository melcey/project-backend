package com.cs308.backend.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.cs308.backend.service.UserService;

@Configuration
// Enables component scanning across the package to register the beans (important for tests)
// In normal use, this should not create any issues since the beans are registered once per context
@ComponentScan(basePackages = "com.cs308.backend.security")
@EnableWebSecurity
@EnableMethodSecurity
public class JwtSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtSecurityConfig(CustomUserDetailsService customUserDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationProvider authenticationProvider)
            throws Exception {
        http
                // Enable CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Disable CSRF as we're using JWT
                .csrf(csrf -> csrf.disable())

                // Set session management to stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // Set permissions on endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/products/**").permitAll()
                        .requestMatchers("/anoncart/**").permitAll()

                        .requestMatchers("/cart/**").hasRole("CUSTOMER")
                        .requestMatchers("/order/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/comment/submit").hasRole("CUSTOMER")
                        .requestMatchers("/rating/**").hasRole("CUSTOMER")
                        .requestMatchers("/payments/**").hasRole("CUSTOMER")
                        .requestMatchers("/wishlist/**").hasRole("CUSTOMER")
                        .requestMatchers("/returns/**").hasRole("CUSTOMER")
                        .requestMatchers("/profile/**").hasRole("CUSTOMER")

                        .requestMatchers("/prodman/**").hasRole("PRODUCT_MANAGER")
                        .requestMatchers("/order/manager/**").hasRole("PRODUCT_MANAGER")
                        .requestMatchers("/comment/approve").hasRole("PRODUCT_MANAGER")
                        .requestMatchers("/comment/disapprove").hasRole("PRODUCT_MANAGER")

                        .requestMatchers("/invoices/**").hasAnyRole("CUSTOMER", "PRODUCT_MANAGER")

                        .requestMatchers("/sales/**").hasRole("SALES_MANAGER")

                        .anyRequest().authenticated())

                .authenticationProvider(authenticationProvider);

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserService userService) {
        return new CustomAuthenticationProvider(userService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}