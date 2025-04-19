package com.cs308.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.UserRepository;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setName("John Doe");
        testUser.setEncryptedEmail("johndoe@example.com".getBytes());
        testUser.setPasswordHashed("password123".getBytes());
        testUser.setRole(Role.customer);
        testUser.setAddress("123 Main St");
    }

    @Test
    void testFindByEmailAndPasswordAndRole() {
        when(userRepository.findByEmailAndPasswordAndRole("johndoe@example.com", "password123", "customer"))
            .thenReturn(Optional.of(testUser));

        Optional<User> user = userService.findByEmailAndPasswordAndRole("johndoe@example.com", "password123", "customer");

        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo("John Doe");
        verify(userRepository, times(1)).findByEmailAndPasswordAndRole("johndoe@example.com", "password123", "customer");
    }

    @Test
    void testFindByEmail() {
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> user = userService.findByEmail("johndoe@example.com");

        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo("John Doe");
        verify(userRepository, times(1)).findByEmail("johndoe@example.com");
    }

    @Test
    void testFindByName() {
        when(userRepository.findByName("John Doe")).thenReturn(List.of(testUser));

        List<User> users = userService.findByName("John Doe");

        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getName()).isEqualTo("John Doe");
        verify(userRepository, times(1)).findByName("John Doe");
    }

    @Test
    void testFindByRole() {
        when(userRepository.findByRole(Role.customer)).thenReturn(List.of(testUser));

        List<User> users = userService.findByRole(Role.customer);

        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getRole()).isEqualTo(Role.customer);
        verify(userRepository, times(1)).findByRole(Role.customer);
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> user = userService.findById(1L);

        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo("John Doe");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testInsertNewUser() {
        when(userRepository.insertNewUser(testUser, "johndoe@example.com", "password123"))
            .thenReturn(Optional.of(testUser));

        Optional<User> user = userService.insertNewUser(testUser, "johndoe@example.com", "password123");

        assertThat(user).isPresent();
        assertThat(user.get().getName()).isEqualTo("John Doe");
        verify(userRepository, times(1)).insertNewUser(testUser, "johndoe@example.com", "password123");
    }

    @Test
    void testDeleteUserById() {
        doNothing().when(userRepository).deleteUserById(testUser);

        userService.deleteUserById(testUser);

        verify(userRepository, times(1)).deleteUserById(testUser);
    }

    @Test
    void testUpdateUserName() {
        // Use thenAnswer to update the name field before returning the object
        when(userRepository.updateUserName(testUser, "John Smith")).thenAnswer(invocation -> {
            testUser.setName("John Smith"); // Update the name field
            return Optional.of(testUser);
        });
    
        Optional<User> updatedUser = userService.updateUserName(testUser, "John Smith");
    
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getName()).isEqualTo("John Smith");
        verify(userRepository, times(1)).updateUserName(testUser, "John Smith");
    }

    @Test
    void testUpdateUserEmail() {
        when(userRepository.updateUserEmail(testUser, "johnsmith@example.com")).thenAnswer(invocation -> {
            testUser.setEncryptedEmail("johnsmith@example.com".getBytes());
            return Optional.of(testUser);
        });

        Optional<User> updatedUser = userService.updateUserEmail(testUser, "johnsmith@example.com");

        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getEncryptedEmail()).isEqualTo("johnsmith@example.com".getBytes());
        verify(userRepository, times(1)).updateUserEmail(testUser, "johnsmith@example.com");
    }

    @Test
    void testUpdateUserAddress() {
        when(userRepository.updateUserAddress(testUser, "456 Elm St")).thenAnswer(invocation -> {
            testUser.setAddress("456 Elm St");
            return Optional.of(testUser);
        });

        Optional<User> updatedUser = userService.updateUserAddress(testUser, "456 Elm St");

        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getAddress()).isEqualTo("456 Elm St");
        verify(userRepository, times(1)).updateUserAddress(testUser, "456 Elm St");
    }

    @Test
    void testUpdateUserPassword() {
        when(userRepository.updateUserPassword(testUser, "newpassword123")).thenAnswer(invocation -> {
            testUser.setPasswordHashed("newpassword123".getBytes());
            return Optional.of(testUser);
        });

        Optional<User> updatedUser = userService.updateUserPassword(testUser, "newpassword123");

        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getPasswordHashed()).isEqualTo("newpassword123".getBytes());
        verify(userRepository, times(1)).updateUserPassword(testUser, "newpassword123");
    }
}