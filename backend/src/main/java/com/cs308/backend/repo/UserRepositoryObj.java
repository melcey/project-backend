package com.cs308.backend.repo;

import java.util.Optional;

import com.cs308.backend.dao.User;

// The specific interface to deal with queries
// for which the entire User objects will need to be passed in Java
public interface UserRepositoryObj {
    // The method to insert a new user into the `users` table
    // The parameters `email` and `password` are the plain strings since they are not going to be stored on the User object
    Optional<User> insertNewUser(User user, String email, String password);

    // The method to delete a user from the `users` table given their ID
    void deleteUserById(User user);

    // The method to update a user's name
    Optional<User> updateUserName(User user, String newName);

    // The method to update a user's email
    Optional<User> updateUserEmail(User user, String newEmail);

    // The method to update a user's address
    Optional<User> updateUserAddress(User user, String newAddress);

    // The method to update a user's password
    Optional<User> updateUserPassword(User user, String newPassword);

    // A user will not be able to update their role (at least for now)
}
