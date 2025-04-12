package com.cs308.backend.dao;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="ratings", schema = "public")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id")
    private Long id;
    // Many ratings-one product
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product ratedProduct;

    // Many ratings-one user
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User ratedUser;

    @Column(name = "rating")
    private int rating;

    @Column(name = "rating_date")
    private LocalDateTime ratingDate;

    public Rating() {
    }

    public Rating(Product ratedProduct, User ratedUser, int rating) {
        this.ratedProduct = ratedProduct;
        this.ratedUser = ratedUser;
        this.rating = rating;
        this.ratingDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getRatedProduct() {
        return ratedProduct;
    }

    public void setRatedProduct(Product ratedProduct) {
        this.ratedProduct = ratedProduct;
    }

    public User getRatedUser() {
        return ratedUser;
    }

    public void setRatedUser(User ratedUser) {
        this.ratedUser = ratedUser;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDateTime getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(LocalDateTime ratingDate) {
        this.ratingDate = ratingDate;
    }

/**
 * CREATE TABLE ratings (
        rating_id SERIAL PRIMARY KEY,
        product_id INT REFERENCES products(product_id),
        user_id INT REFERENCES users(user_id),
        rating INT CHECK (rating BETWEEN 1 AND 10),
        rating_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
 */

    
}
