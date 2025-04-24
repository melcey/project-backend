package com.cs308.backend.dto;

import java.util.List;

public class RatingListResponse {
    private List<RatingResponse> ratings;

    public RatingListResponse() {}

    public RatingListResponse(List<RatingResponse> ratings) {
        this.ratings = ratings;
    }

    public List<RatingResponse> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingResponse> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RatingListResponse [ratings=").append(ratings)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ratings == null) ? 0 : ratings.hashCode());
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
        RatingListResponse other = (RatingListResponse) obj;
        if (ratings == null) {
            if (other.ratings != null)
                return false;
        } else if (!ratings.equals(other.ratings))
            return false;
        return true;
    }
}
