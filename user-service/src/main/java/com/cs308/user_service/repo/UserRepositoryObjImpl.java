package com.cs308.user_service.repo;

import com.cs308.user_service.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// The class to implement the interface UserRepositoryObj,
// which deals with queries where User objects are going to be passed
// Follows the naming convention; therefore, Spring Data is going to wire this up into the UserRepositoryObj interface automatically
// Since the UserRepository also extends the UserRepositoryObj interface, these functions will be the ones running when a UserRepository instance invokes them
public class UserRepositoryObjImpl implements UserRepositoryObj {
    // EntityManager will execute the generated native SQL query on the database
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User insertNewUser(User user, String email, String password) {
        
        // Creates the query command in SQL to insert the new record to the table and return the inserted data
        String sqlQuery = "INSERT INTO users (name, email, home_address, password_hash, role) VALUES (:name, crypt(:email, gen_salt('bf')), :address, crypt(:password, gen_salt('bf')), :role) RETURNING *";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a User object
        User newUser = (User)entityManager.createNativeQuery(sqlQuery, User.class)
            .setParameter("name", user.getName())
            .setParameter("email", email)
            .setParameter("address", user.getAddress())
            .setParameter("password", password)
            .setParameter("role", user.getRole())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return newUser;
    }

    @Override
    public void deleteUserById(User user) {
        // Creates the query command to delete the user with the given ID
        String sqlQuery = "DELETE FROM users WHERE user_id = :id";
        
        // Creates the native query, injects the parameters, and executes the query
        entityManager.createNativeQuery(sqlQuery)
            .setParameter("id", user.getId())
            .executeUpdate();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
    }

    @Override
    public User updateUserName(User user, String newName) {
        // Creates the query command to update the name of the given user
        String sqlQuery = "UPDATE users SET name = :new_name WHERE user_id = :id";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a User object
        User updatedUser = (User)entityManager.createNativeQuery(sqlQuery, User.class)
            .setParameter("new_name", newName)
            .setParameter("id", user.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedUser;
    }

    @Override
    public User updateUserEmail(User user, String newEmail) {
        // Creates the query command to update the email of the given user
        String sqlQuery = "UPDATE users SET email = crypt(:new_email, gen_salt('bf')) WHERE user_id = :id";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a User object
        User updatedUser = (User)entityManager.createNativeQuery(sqlQuery, User.class)
            .setParameter("new_email", newEmail)
            .setParameter("id", user.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedUser;
    }

    @Override
    public User updateUserAddress(User user, String newAddress) {
        // Creates the query command to update the address of the given user
        String sqlQuery = "UPDATE users SET home_address = :new_address WHERE user_id = :id";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a User object
        User updatedUser = (User)entityManager.createNativeQuery(sqlQuery, User.class)
            .setParameter("new_address", newAddress)
            .setParameter("id", user.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedUser;
    }

    @Override
    public User updateUserPassword(User user, String newPassword) {
        // Creates the query command to update the password of the given user
        String sqlQuery = "UPDATE users SET password_hash = crypt(:new_password, gen_salt('bf')) WHERE user_id = :id";
        
        // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a User object
        User updatedUser = (User)entityManager.createNativeQuery(sqlQuery, User.class)
            .setParameter("new_password", newPassword)
            .setParameter("id", user.getId())
            .getSingleResult();

        // Pending changes are written to the database
        entityManager.flush();
        // The persistence context is cleared so that fresh data can be returned from the database in subsequent queries
        entityManager.clear();
        
        // Returns the retrieved result
        return updatedUser;
    }
}
