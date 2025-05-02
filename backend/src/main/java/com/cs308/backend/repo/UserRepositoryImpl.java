package com.cs308.backend.repo;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cs308.backend.dao.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// The class to implement the interface UserRepositoryObj,
// which deals with queries where User objects are going to be passed
// Follows the naming convention; therefore, Spring Data is going to wire this up into the UserRepositoryObj interface automatically
// Since the UserRepository also extends the UserRepositoryObj interface, these functions will be the ones running when a UserRepository instance invokes them
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepositoryObj {
    // EntityManager will execute the generated native SQL query on the database
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<String> getEmail(User user) {
        String sqlQuery = "SELECT pgp_sym_decrypt(email, :encryption_key) AS decrypted_email FROM users WHERE user_id = :id";

        try {
            String email = (String) entityManager.createNativeQuery(sqlQuery)
                .setParameter("encryption_key", "eUvloSq81xH5J2FjEOzDhKSYwp/e8VYetV8lPwjlWmM=")
                .setParameter("id", user.getId())
                .getSingleResult();

            return Optional.of(email);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> insertNewUser(User user, String email, String password) {
        // Creates the query command in SQL to insert the new record to the table and return the inserted data
        // Casts the result from crypt() into BYTEA
        String sqlQuery = "INSERT INTO users (name, email, home_address, password_hash, role) VALUES (:name, pgp_sym_encrypt(:email, :encryption_key), :address, crypt(:password, gen_salt('bf'))::bytea, :role) RETURNING *";

        try {
            // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a User object
            User newUser = (User)entityManager.createNativeQuery(sqlQuery, User.class)
                .setParameter("name", user.getName())
                .setParameter("email", email)
                .setParameter("encryption_key", "eUvloSq81xH5J2FjEOzDhKSYwp/e8VYetV8lPwjlWmM=")
                .setParameter("address", user.getAddress())
                .setParameter("password", password)
                .setParameter("role", user.getRole().getValue())
                .getSingleResult();

            // Pending changes are written to the database
            entityManager.flush();
            entityManager.refresh(newUser);

            // Returns the retrieved result
            return Optional.of(newUser);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
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
    public Optional<User> updateUserName(User user, String newName) {
        // Creates the query command to update the name of the given user
        String sqlQuery = "UPDATE users SET name = :new_name WHERE user_id = :id RETURNING *";

        try {
            // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a User object
            User updatedUser = (User)entityManager.createNativeQuery(sqlQuery, User.class)
                .setParameter("new_name", newName)
                .setParameter("id", user.getId())
                .getSingleResult();

            // Pending changes are written to the database
            entityManager.flush();
            entityManager.refresh(updatedUser);

            // Returns the retrieved result
            return Optional.of(updatedUser);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> updateUserEmail(User user, String newEmail) {
        // Creates the query command to update the email of the given user
        String sqlQuery = "UPDATE users SET email = pgp_sym_encrypt(:new_email, :encryption_key) WHERE user_id = :id RETURNING *";

        try {
            // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a User object
            User updatedUser = (User)entityManager.createNativeQuery(sqlQuery, User.class)
                .setParameter("new_email", newEmail)
                .setParameter("encryption_key", "eUvloSq81xH5J2FjEOzDhKSYwp/e8VYetV8lPwjlWmM=")
                .setParameter("id", user.getId())
                .getSingleResult();

            // Pending changes are written to the database
            entityManager.flush();
            entityManager.refresh(updatedUser);

            // Returns the retrieved result
            return Optional.of(updatedUser);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> updateUserAddress(User user, String newAddress) {
        // Creates the query command to update the address of the given user
        String sqlQuery = "UPDATE users SET home_address = :new_address WHERE user_id = :id RETURNING *";

        try {
            // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a User object
            User updatedUser = (User)entityManager.createNativeQuery(sqlQuery, User.class)
                .setParameter("new_address", newAddress)
                .setParameter("id", user.getId())
                .getSingleResult();

            // Pending changes are written to the database
            entityManager.flush();
            entityManager.refresh(updatedUser);

            // Returns the retrieved result
            return Optional.of(updatedUser);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> updateUserPassword(User user, String newPassword) {
        // Creates the query command to update the password of the given user
        String sqlQuery = "UPDATE users SET password_hash = crypt(:new_password, gen_salt('bf'))::bytea WHERE user_id = :id RETURNING *";

        try {
            // Creates the native query, injects the parameters, executes the query, and retrieves the result casted into a User object
            User updatedUser = (User)entityManager.createNativeQuery(sqlQuery, User.class)
                .setParameter("new_password", newPassword)
                .setParameter("id", user.getId())
                .getSingleResult();

            // Pending changes are written to the database
            entityManager.flush();

            entityManager.refresh(updatedUser);
            // Returns the retrieved result
            return Optional.of(updatedUser);
        }
        catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
