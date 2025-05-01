package com.cs308.backend.dto;

import com.cs308.backend.dao.ReturnRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReturnRequestResponse {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal originalPrice;
    private BigDecimal totalRefundAmount;
    private String reason;
    private String status;
    private LocalDateTime requestDate;
    private LocalDateTime resolutionDate;
    private String resolvedBy;

    public ReturnRequestResponse() {}
    
    public ReturnRequestResponse(Long id, Long orderId, Long productId, String productName, int quantity,
            BigDecimal originalPrice, BigDecimal totalRefundAmount, String reason, String status,
            LocalDateTime requestDate, LocalDateTime resolutionDate, String resolvedBy) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.totalRefundAmount = totalRefundAmount;
        this.reason = reason;
        this.status = status;
        this.requestDate = requestDate;
        this.resolutionDate = resolutionDate;
        this.resolvedBy = resolvedBy;
    }

    public ReturnRequestResponse(ReturnRequest request) {
        this.id = request.getId();
        this.orderId = request.getOrder().getId();
        this.productId = request.getProduct().getId();
        this.productName = request.getProduct().getName();
        this.quantity = request.getQuantity();
        this.originalPrice = request.getOriginalPrice();
        this.totalRefundAmount = request.getOriginalPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        this.reason = request.getReason();
        this.status = request.getStatus().toString();
        this.requestDate = request.getRequestDate();
        this.resolutionDate = request.getResolutionDate();
        this.resolvedBy = request.getResolvedBy() != null ? request.getResolvedBy().getName() : null;
    }
    
    // Getters and setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }
    
    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }
    
    public BigDecimal getTotalRefundAmount() {
        return totalRefundAmount;
    }
    
    public void setTotalRefundAmount(BigDecimal totalRefundAmount) {
        this.totalRefundAmount = totalRefundAmount;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getRequestDate() {
        return requestDate;
    }
    
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
    
    public LocalDateTime getResolutionDate() {
        return resolutionDate;
    }
    
    public void setResolutionDate(LocalDateTime resolutionDate) {
        this.resolutionDate = resolutionDate;
    }
    
    public String getResolvedBy() {
        return resolvedBy;
    }
    
    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ReturnRequestResponse [id=").append(id)
            .append(", orderId=").append(orderId)
            .append(", productId=").append(productId)
            .append(", productName=").append(productName)
            .append(", quantity=").append(quantity)
            .append(", originalPrice=").append(originalPrice)
            .append(", totalRefundAmount=").append(totalRefundAmount)
            .append(", reason=").append(reason)
            .append(", status=").append(status)
            .append(", requestDate=").append(requestDate)
            .append(", resolutionDate=").append(resolutionDate)
            .append(", resolvedBy=").append(resolvedBy)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
        result = prime * result + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result + ((productName == null) ? 0 : productName.hashCode());
        result = prime * result + quantity;
        result = prime * result + ((originalPrice == null) ? 0 : originalPrice.hashCode());
        result = prime * result + ((totalRefundAmount == null) ? 0 : totalRefundAmount.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((requestDate == null) ? 0 : requestDate.hashCode());
        result = prime * result + ((resolutionDate == null) ? 0 : resolutionDate.hashCode());
        result = prime * result + ((resolvedBy == null) ? 0 : resolvedBy.hashCode());
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
        ReturnRequestResponse other = (ReturnRequestResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (orderId == null) {
            if (other.orderId != null)
                return false;
        } else if (!orderId.equals(other.orderId))
            return false;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        if (productName == null) {
            if (other.productName != null)
                return false;
        } else if (!productName.equals(other.productName))
            return false;
        if (quantity != other.quantity)
            return false;
        if (originalPrice == null) {
            if (other.originalPrice != null)
                return false;
        } else if (!originalPrice.equals(other.originalPrice))
            return false;
        if (totalRefundAmount == null) {
            if (other.totalRefundAmount != null)
                return false;
        } else if (!totalRefundAmount.equals(other.totalRefundAmount))
            return false;
        if (reason == null) {
            if (other.reason != null)
                return false;
        } else if (!reason.equals(other.reason))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (requestDate == null) {
            if (other.requestDate != null)
                return false;
        } else if (!requestDate.equals(other.requestDate))
            return false;
        if (resolutionDate == null) {
            if (other.resolutionDate != null)
                return false;
        } else if (!resolutionDate.equals(other.resolutionDate))
            return false;
        if (resolvedBy == null) {
            if (other.resolvedBy != null)
                return false;
        } else if (!resolvedBy.equals(other.resolvedBy))
            return false;
        return true;
    }
}