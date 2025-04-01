package com.cs308.backend.dto;

import java.math.BigDecimal;

public class ProductResponse {
    private Long id;
    private String name;
    private String model;
    private String serialNumber;
    private String description;
    private int quantityInStock;
    private BigDecimal price;
    private String warrantyStatus;
    private String distributorInfo;
    private boolean isActive;
    private String imageUrl;
    private CategoryResponse category;

    public ProductResponse() {
        this.id = null;
        this.name = null;
        this.model = null;
        this.serialNumber = null;
        this.description = null;
        this.quantityInStock = 0;
        this.price = null;
        this.warrantyStatus = null;
        this.distributorInfo = null;
        this.imageUrl = null;
        this.category = new CategoryResponse();
    }

    public ProductResponse(Long id, String name, String model, String serialNumber, String description,
            int quantityInStock, BigDecimal price, String warrantyStatus, String distributorInfo, boolean isActive,
            String imageUrl, CategoryResponse category) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.serialNumber = serialNumber;
        this.description = description;
        this.quantityInStock = quantityInStock;
        this.price = price;
        this.warrantyStatus = warrantyStatus;
        this.distributorInfo = distributorInfo;
        this.isActive = isActive;
        this.imageUrl = imageUrl;
        this.category = category;
    }

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getWarrantyStatus() {
        return warrantyStatus;
    }

    public void setWarrantyStatus(String warrantyStatus) {
        this.warrantyStatus = warrantyStatus;
    }

    public String getDistributorInfo() {
        return distributorInfo;
    }

    public void setDistributorInfo(String distributorInfo) {
        this.distributorInfo = distributorInfo;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    
}