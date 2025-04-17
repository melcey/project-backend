package com.cs308.backend.dao;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// The entity User
// Corresponds to the `users` table in the database
@Entity
@Table(name = "users", schema = "public")
public class User {
    // The ID of the user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    
    // The name of the user
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    // The encrypted email address of the user
    @Column(name = "email")
    private byte[] encryptedEmail;
    
    // The address of the user
    @Column(name = "home_address")
    private String address;

    // The encrypted password of the user
    @Column(name = "password_hash")
    private byte[] passwordHashed;

    // The role of the user
    @Column(name = "role", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Role role;

    // The products (to be used when the role is product manager)
    // One-to-many association mapped by the "productManager" field in the Product class
    @OneToMany(mappedBy = "productManager")
    private Set<Product> managedProducts;

    // The default constructor
    public User(Long userId) {
        this.id = null;
        this.name = null;
        this.encryptedEmail = null;
        this.address = null;
        this.passwordHashed = null;
        this.role = null;
        this.managedProducts = new HashSet<>();
    }

    // The constructor with all fields other than the managed products filled
    public User(Long id, String name, byte[] encrpytedEmail, String address, byte[] passwordHashed, Role role) {
        this.id = id;
        this.name = name;
        this.encryptedEmail = encrpytedEmail;
        this.address = address;
        this.passwordHashed = passwordHashed;
        this.role = role;
        this.managedProducts = new HashSet<>();
    }

    // The constructor to be used
    // Email and password will be given separately while constructing a User object in later stages of the project
    public User(String name, String address, Role role) {
        this.id = null;
        this.name = name;
        this.encryptedEmail = null;
        this.address = address;
        this.passwordHashed = null;
        this.role = role;
        this.managedProducts = new HashSet<>();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getEncryptedEmail() {
        return encryptedEmail;
    }

    public void setEncryptedEmail(byte[] encrpytedEmail) {
        this.encryptedEmail = encrpytedEmail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String homeAddress) {
        this.address = homeAddress;
    }

    public byte[] getPasswordHashed() {
        return passwordHashed;
    }

    public void setPasswordHashed(byte[] passwordHashed) {
        this.passwordHashed = passwordHashed;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // toString() method overridden for User
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User [id=").append(id)
            .append(", name=").append(name)
            .append( ", address=").append(address);

        if (role != null) {
            builder.append(", role=").append(role.toString());
        }
        else {
            builder.append(", role=").append("null");
        }

        builder.append( "]");
            
        return builder.toString();
    }
    
    // hashCode() method overridden for User
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    // equals() method overridden for User
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    public Set<Product> getManagedProducts() {
        return managedProducts;
    }

    public void setManagedProducts(Set<Product> managedProducts) {
        this.managedProducts = managedProducts;
    }
}
