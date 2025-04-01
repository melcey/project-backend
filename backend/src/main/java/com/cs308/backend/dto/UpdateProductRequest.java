package com.cs308.backend.dto;

import java.math.BigDecimal;

public class UpdateProductRequest {
    // Each field is optional, which is why wrapper types are used for primitive types
    private String name;
    private String model;
    private String serialNumber;
    private String description;
    private Integer quantityInStock;
    private BigDecimal price;
    private String warrantyStatus;
    private String distributorInfo;
    private Boolean isActive;
    private String imageUrl;
    private String category;

    public UpdateProductRequest() {
        this.name = null;
        this.model = null;
        this.serialNumber = null;
        this.description = null;
        this.quantityInStock = null;
        this.price = null;
        this.warrantyStatus = null;
        this.distributorInfo = null;
        this.imageUrl = null;
        this.category = null;
    }

    public UpdateProductRequest(String name, String model, String serialNumber, String description,
            Integer quantityInStock, BigDecimal price, String warrantyStatus, String distributorInfo, Boolean isActive,
            String imageUrl, String category) {
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

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
}