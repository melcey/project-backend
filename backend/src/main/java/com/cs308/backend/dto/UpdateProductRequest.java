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
    private Long categoryId;

    public UpdateProductRequest() {}

    public UpdateProductRequest(String name, String model, String serialNumber, String description,
            Integer quantityInStock, BigDecimal price, String warrantyStatus, String distributorInfo, Boolean isActive,
            String imageUrl, Long categoryId) {
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
        this.categoryId = categoryId;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UpdateProductRequest [name=").append(name)
            .append(", model=").append(model)
            .append(", serialNumber=").append(serialNumber)
            .append(", description=").append(description)
            .append(", quantityInStock=").append(quantityInStock)
            .append(", price=").append(price)
            .append(", warrantyStatus=").append(warrantyStatus)
            .append(", distributorInfo=").append(distributorInfo)
            .append(", isActive=").append(isActive)
            .append(", imageUrl=").append(imageUrl)
            .append(", categoryId=").append(categoryId)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((model == null) ? 0 : model.hashCode());
        result = prime * result + ((serialNumber == null) ? 0 : serialNumber.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((quantityInStock == null) ? 0 : quantityInStock.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((warrantyStatus == null) ? 0 : warrantyStatus.hashCode());
        result = prime * result + ((distributorInfo == null) ? 0 : distributorInfo.hashCode());
        result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
        result = prime * result + ((imageUrl == null) ? 0 : imageUrl.hashCode());
        result = prime * result + ((categoryId == null) ? 0 : categoryId.hashCode());
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
        UpdateProductRequest other = (UpdateProductRequest) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (model == null) {
            if (other.model != null)
                return false;
        } else if (!model.equals(other.model))
            return false;
        if (serialNumber == null) {
            if (other.serialNumber != null)
                return false;
        } else if (!serialNumber.equals(other.serialNumber))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (quantityInStock == null) {
            if (other.quantityInStock != null)
                return false;
        } else if (!quantityInStock.equals(other.quantityInStock))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (warrantyStatus == null) {
            if (other.warrantyStatus != null)
                return false;
        } else if (!warrantyStatus.equals(other.warrantyStatus))
            return false;
        if (distributorInfo == null) {
            if (other.distributorInfo != null)
                return false;
        } else if (!distributorInfo.equals(other.distributorInfo))
            return false;
        if (isActive == null) {
            if (other.isActive != null)
                return false;
        } else if (!isActive.equals(other.isActive))
            return false;
        if (imageUrl == null) {
            if (other.imageUrl != null)
                return false;
        } else if (!imageUrl.equals(other.imageUrl))
            return false;
        if (categoryId == null) {
            if (other.categoryId != null)
                return false;
        } else if (!categoryId.equals(other.categoryId))
            return false;
        return true;
    }
}