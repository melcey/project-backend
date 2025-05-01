package com.cs308.backend.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cs308.backend.dao.User;

public class UserPrincipal implements UserDetails {
    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    private UserPrincipal(User user) {
        this.user = user;
        // Convert user role to Spring Security authority
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority(String.format("ROLE_%s", user.getRole().name().toUpperCase())));
    }

    // Factory method to create UserPrincipal from User
    public static UserPrincipal create(User user) {
        return new UserPrincipal(user);
    }

    // Get the underlying user
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        // Note: This will need to be modified once we implement proper password
        // handling
        return new String(user.getPasswordHashed());
    }

    @Override
    public String getUsername() {
        // Using email as username
        return new String(user.getEncryptedEmail());
    }

    // Account status methods - you can customize these based on your needs
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Add equals and hashCode methods
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserPrincipal that = (UserPrincipal) o;
        return user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return user.hashCode();
    }
}