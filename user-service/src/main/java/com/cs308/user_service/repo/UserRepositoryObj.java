package com.cs308.user_service.repo;

import com.cs308.user_service.model.User;

// The specific interface to deal with queries
// for which the entire User objects will need to be passed in Java
public interface UserRepositoryObj {
    // The method to insert a new user into the `users` table
    // The parameters `email` and `password` are the plain strings since they are not going to be stored on the User object
    User insertNewUser(User user, String email, String password);

    // The method to delete a user from the `users` table given their ID
    void deleteUserById(User user);
}
