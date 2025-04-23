package com.cs308.backend.dto;

public class SubmitRatingRequest {
    private Long productId;
    private int ratingValue;

    public SubmitRatingRequest() {}

    public SubmitRatingRequest(Long productId, int ratingValue) {
        this.productId = productId;
        this.ratingValue = ratingValue;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SubmitRatingRequest [productId=").append(productId)
            .append(", ratingValue=").append(ratingValue)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + ratingValue;
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
        SubmitRatingRequest other = (SubmitRatingRequest) obj;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        if (ratingValue != other.ratingValue)
            return false;
        return true;
    }
}
