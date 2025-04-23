package com.cs308.backend.dto;

import java.time.LocalDateTime;

public class RatingResponse {
    private Long id;
    private ProductResponse product;
    private Long userId;
    private int rating;
    private LocalDateTime ratingDate;
    
    public RatingResponse() {}

    public RatingResponse(Long id, ProductResponse product, Long userId, int rating, LocalDateTime ratingDate) {
        this.id = id;
        this.product = product;
        this.userId = userId;
        this.rating = rating;
        this.ratingDate = ratingDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        builder.append("RatingResponse [id=").append(id)
            .append(", product=").append(product)
            .append(", userId=").append(userId)
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
        result = prime * result + ((product == null) ? 0 : product.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + rating;
        result = prime * result + ((ratingDate == null) ? 0 : ratingDate.hashCode());
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
        RatingResponse other = (RatingResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (rating != other.rating)
            return false;
        if (ratingDate == null) {
            if (other.ratingDate != null)
                return false;
        } else if (!ratingDate.equals(other.ratingDate))
            return false;
        return true;
    }
}
