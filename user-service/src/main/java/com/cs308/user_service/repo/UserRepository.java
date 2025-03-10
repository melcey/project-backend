package com.cs308.user_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cs308.user_service.model.Role;
import com.cs308.user_service.model.User;
import java.util.List;
import java.util.Optional;

// Extending both JpaRepository<User, Long> for findBy... queries
// and UserRepositoryObj to be able to deal with User objects passed as paramters
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryObj {
    // SELECT queries:
    // The actual query to log in
    // An Optional<User> object may or may not contain a User object
    @Query(value = "SELECT * FROM users WHERE email = crypt(:email, email) AND password_hash = crypt(:password, password_hash) AND role  = :role", nativeQuery = true)
    Optional<User> findByEmailAndPasswordAndRole(@Param("email") String email, @Param("password") String password, @Param("role") Role role);

    // Executes a native SQL query on the database (specified with the nativeQuery parameter)
    // :email is the query parameter given in Java
    // `email` is the hashed email in the database
    // PostgreSQL rehashes :email using the same salt as `email` to check
    // We are considering `email` unique
    // An Optional<User> object may or may not contain a User object
    @Query(value = "SELECT * FROM users WHERE email = crypt(:email, email)", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
    
    List<User> findByName(String name);

    List<User> findByRole(Role role);

    List<User> findByAddress(String address);

    // What to do for multiple attribute search in API (if we need ever):
    // Run each query for the filters => Return the intersection
    // Alternative: Such queries can be implemented if needed
}
