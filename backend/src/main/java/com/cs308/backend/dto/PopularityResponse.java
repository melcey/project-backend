package com.cs308.backend.dto;

public class PopularityResponse {
    private int popularity;
    private ProductResponse product;

    public PopularityResponse() {}

    public PopularityResponse(int popularity, ProductResponse product) {
        this.popularity = popularity;
        this.product = product;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PopularityResponse [popularity=").append(popularity)
            .append(", product=").append(product)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + popularity;
        result = prime * result + ((product == null) ? 0 : product.hashCode());
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
        PopularityResponse other = (PopularityResponse) obj;
        if (popularity != other.popularity)
            return false;
        if (product == null) {
            if (other.product != null)
                return false;
        } else if (!product.equals(other.product))
            return false;
        return true;
    }
}
