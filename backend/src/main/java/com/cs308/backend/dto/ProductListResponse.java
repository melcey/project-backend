package com.cs308.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductListResponse {
    private List<ProductResponse> products;

    public ProductListResponse() {
        products = new ArrayList<>();
    }

    public ProductListResponse(List<ProductResponse> products) {
        this.products = products;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }
}