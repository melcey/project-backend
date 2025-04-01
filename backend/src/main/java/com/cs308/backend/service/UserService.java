package com.cs308.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmailAndPasswordAndRole(String email, String password, String role) {
        return userRepository.findByEmailAndPasswordAndRole(email, password, role);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByIdAndRole(Long id, Role role) {
        return userRepository.findByIdAndRole(id, role.getValue());
    }
    
    public User insertNewUser(User user, String email, String password) {
        return userRepository.insertNewUser(user, email, password);
    }

    public void deleteUserById(User user) {
        userRepository.deleteUserById(user);
    }

    public User updateUserName(User user, String newName) {
        return userRepository.updateUserName(user, newName);
    }

    public User updateUserEmail(User user, String newEmail) {
        return userRepository.updateUserEmail(user, newEmail);
    }

    public User updateUserAddress(User user, String newAddress) {
        return userRepository.updateUserAddress(user, newAddress);
    }

    public User updateUserPassword(User user, String newPassword) {
        return userRepository.updateUserPassword(user, newPassword);
    }
}
