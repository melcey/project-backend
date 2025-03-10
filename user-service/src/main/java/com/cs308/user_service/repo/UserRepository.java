package com.cs308.user_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cs308.user_service.model.Role;
import com.cs308.user_service.model.User;
import java.util.List;

// Extending both JpaRepository<User, Long> for findBy... queries
// and UserRepositoryObj to be able to deal with User objects passed as paramters
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryObj {
    // SELECT queries:

    // Executes a native SQL query on the database (specified with the nativeQuery parameter)
    // :email is the query parameter given in Java
    // `email` is the hashed email in the database
    // PostgreSQL rehashes :email using the same salt as `email` to check
    @Query(value = "SELECT * FROM users WHERE email = crypt(:email, email)", nativeQuery = true)
    List<User> findByEmail(@Param("email") String email);
    
    List<User> findByName(String name);

    List<User> findByRole(Role role);

    List<User> findByAddress(String address);

    @Query(value = "SELECT * FROM users WHERE email = crypt(:email, email) AND name = :name", nativeQuery = true)
    List<User> findByEmailAndName(@Param("email") String email, @Param("name") String name);

    @Query(value = "SELECT * FROM users WHERE email = crypt(:email, email) AND role = :role", nativeQuery = true)
    List<User> findByEmailAndRole(@Param("email") String email, @Param("role") Role role);

    @Query(value = "SELECT * FROM users WHERE email = crypt(:email, email) AND home_address = :address", nativeQuery = true)
    List<User> findByEmailAndAddress(@Param("email") String email, @Param("address") String address);

    List<User> findByNameAndRole(String name, Role role);

    List<User> findByNameAndAddress(String name, String address);

    List<User> findByRoleAndAddress(Role role, String address);

    @Query(value = "SELECT * FROM users WHERE email = crypt(:email, email) AND name = :name AND role = :role", nativeQuery = true)
    List<User> findByEmailAndNameAndRole(@Param("email") String email, @Param("name") String name, @Param("role") Role role);

    @Query(value = "SELECT * FROM users WHERE email = crypt(:email, email) AND name = :name AND home_address = :address", nativeQuery = true)
    List<User> findByEmailAndNameAndAddress(@Param("email") String email, @Param("name") String name, @Param("address") String address);

    @Query(value = "SELECT * FROM users WHERE email = crypt(:email, email) AND role = :role AND home_address = :address", nativeQuery = true)
    List<User> findByEmailAndRoleAndAddress(@Param("email") String email, @Param("role") Role role, @Param("address") String address);

    List<User> findByNameAndRoleAndAddress(String name, Role role, String address);

    @Query(value = "SELECT * FROM users WHERE email = crypt(:email, email) AND name = :name AND role = :role AND home_address = :address", nativeQuery = true)
    List<User> findByEmailAndNameAndRoleAndAddress(@Param("email") String email, @Param("name") String name, @Param("role") Role role, @Param("address") String address);
}
