package com.cs308.backend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;

// Extending both JpaRepository<User, Long> for findBy... queries
// and UserRepositoryObj to be able to deal with User objects passed as parameters
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryObj {
    // SELECT queries:
    // The actual SQL query to log in
    // An Optional<User> object may or may not contain a User object
    // Results from crypt() are casted into the type BYTEA
    // String value of the Role is passed here in order not to lose PostgreSQL's functionality for crypt()
    @Query(value = "SELECT * FROM users WHERE pgp_sym_decrypt(email, 'eUvloSq81xH5J2FjEOzDhKSYwp/e8VYetV8lPwjlWmM=') = :email AND password_hash = crypt(:password, convert_from(password_hash, 'UTF8'))::bytea AND role = :role", nativeQuery = true)
    Optional<User> findByEmailAndPasswordAndRole(@Param("email") String email, @Param("password") String password, @Param("role") String role);

    // Executes a native SQL query on the database (specified with the nativeQuery parameter)
    // :email is the query parameter given in Java
    // `email` is the hashed email in the database
    // PostgreSQL rehashes :email using the same salt as `email` to check
    // We are considering `email` unique
    // An Optional<User> object may or may not contain a User object
    @Query(value = "SELECT * FROM users WHERE pgp_sym_decrypt(email, 'eUvloSq81xH5J2FjEOzDhKSYwp/e8VYetV8lPwjlWmM=') = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
    
    List<User> findByName(String name);

    // Here, a JPQL query is used in order to leverage the Role enum in Java
    @Query(value = "SELECT u FROM User u WHERE u.role = :role", nativeQuery = false)
    List<User> findByRole(@Param("role") Role role);

    List<User> findByAddress(String address);

    Optional<User> findById(Long id);

    @Query(value = "SELECT * FROM users WHERE user_id = :id AND role = :role", nativeQuery = true)
    Optional<User> findByIdAndRole(@Param("id") Long id, @Param("role") String role);

    // What to do for multiple attribute search in API (if we need ever):
    // Run each query for the filters => Return the intersection
    // Alternative: Such queries can be implemented if needed
}
