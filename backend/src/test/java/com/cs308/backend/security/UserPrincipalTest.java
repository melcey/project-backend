package com.cs308.backend.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;

public class UserPrincipalTest {

    private User testUser;
    private UserPrincipal userPrincipal;

    @BeforeEach
    void setUp() {
        // Initialize a test User
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");
        testUser.setEncryptedEmail("johndoe@example.com".getBytes());
        testUser.setPasswordHashed("password123".getBytes());
        testUser.setRole(Role.customer);

        // Create a UserPrincipal from the test User
        userPrincipal = UserPrincipal.create(testUser);
    }

    @Test
    void testGetUser() {
        // Verify that the underlying User object is correctly wrapped
        assertThat(userPrincipal.getUser()).isEqualTo(testUser);
    }

    @Test
    void testGetAuthorities() {
        // Verify that the authorities are correctly derived from the User's role
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_CUSTOMER");
    }

    @Test
    void testGetPassword() {
        // Verify that the password is correctly returned
        assertThat(userPrincipal.getPassword()).isEqualTo("password123");
    }

    @Test
    void testGetUsername() {
        // Verify that the username (email) is correctly returned
        assertThat(userPrincipal.getUsername()).isEqualTo("johndoe@example.com");
    }

    @Test
    void testAccountStatusMethods() {
        // Verify that the account status methods return the expected values
        assertThat(userPrincipal.isAccountNonExpired()).isTrue();
        assertThat(userPrincipal.isAccountNonLocked()).isTrue();
        assertThat(userPrincipal.isCredentialsNonExpired()).isTrue();
        assertThat(userPrincipal.isEnabled()).isTrue();
    }

    @Test
    void testEqualsAndHashCode() {
        // Create another UserPrincipal with the same User
        UserPrincipal anotherUserPrincipal = UserPrincipal.create(testUser);

        // Verify that equals and hashCode work as expected
        assertThat(userPrincipal).isEqualTo(anotherUserPrincipal);
        assertThat(userPrincipal.hashCode()).isEqualTo(anotherUserPrincipal.hashCode());

        // Create a different User and UserPrincipal
        User differentUser = new User();
        differentUser.setId(2L);
        differentUser.setName("Jane Doe");
        differentUser.setEncryptedEmail("janedoe@example.com".getBytes());
        differentUser.setPasswordHashed("password456".getBytes());
        differentUser.setRole(Role.sales_manager);

        UserPrincipal differentUserPrincipal = UserPrincipal.create(differentUser);

        // Verify that equals and hashCode return false for different users
        assertThat(userPrincipal).isNotEqualTo(differentUserPrincipal);
        assertThat(userPrincipal.hashCode()).isNotEqualTo(differentUserPrincipal.hashCode());
    }
}