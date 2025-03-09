package com.cs308.user_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    // The default constructor
    public User() {}

    // The actual constructor which will be used
    public User(Long id, String name, byte[] encrpytedEmail, String homeAddress, byte[] passwordHashed, Role role) {
        this.id = id;
        this.name = name;
        this.encryptedEmail = encrpytedEmail;
        this.address = homeAddress;
        this.passwordHashed = passwordHashed;
        this.role = role;
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
        return "User [id=" + id + ", name=" + name + ", address=" + address + ", role=" + role + "]";
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
}
