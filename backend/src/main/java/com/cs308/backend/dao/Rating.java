package com.cs308.backend.dao;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private User ratingUser;

    @Column(name = "rating")
    private int rating;

    @Column(name = "rating_date")
    private LocalDateTime ratingDate;

    public Rating() {
    }

    public Rating(Product ratedProduct, User ratedUser, int rating) {
        this.ratedProduct = ratedProduct;
        this.ratingUser = ratedUser;
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

    public User getRatingUser() {
        return ratingUser;
    }

    public void setRatingUser(User ratedUser) {
        this.ratingUser = ratedUser;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Rating [id=").append(id)
            .append(", ratedProduct=").append(ratedProduct)
            .append(", ratedUser=").append(ratingUser)
            .append(", rating=").append(rating)
            .append(", ratingDate=").append(ratingDate)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Rating other = (Rating) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
