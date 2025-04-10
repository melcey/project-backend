package com.cs308.backend.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SearchProductRequest {
    private String name;
    private String model;
    private String serialNumber;
    private String description;
    private String distributorInfo;
    private Boolean isActive;
    private String warrantyStatus;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minQuantity;
    private Integer maxQuantity;
    private List<Long> categoryIds;

    public SearchProductRequest() {
        categoryIds = new ArrayList<>();
    }
    
    public SearchProductRequest(String name, String model, String serialNumber, String description,
            String distributorInfo, Boolean isActive, String warrantyStatus, BigDecimal minPrice, BigDecimal maxPrice,
            Integer minQuantity, Integer maxQuantity, List<Long> categoryIds) {
        this.name = name;
        this.model = model;
        this.serialNumber = serialNumber;
        this.description = description;
        this.distributorInfo = distributorInfo;
        this.isActive = isActive;
        this.warrantyStatus = warrantyStatus;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.categoryIds = categoryIds;
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

    public String getWarrantyStatus() {
        return warrantyStatus;
    }

    public void setWarrantyStatus(String warrantyStatus) {
        this.warrantyStatus = warrantyStatus;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    
}
