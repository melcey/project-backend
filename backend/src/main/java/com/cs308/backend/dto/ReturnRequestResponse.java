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
    
    public ReturnRequestResponse(ReturnRequest request) {
        this.id = request.getId();
        this.orderId = request.getOrder().getId();
        this.productId = request.getProduct().getId();
        this.productName = request.getProduct().getName();
        this.quantity = request.getQuantity();
        this.originalPrice = request.getOriginalPrice();
        this.totalRefundAmount = request.getOriginalPrice().multiply(new BigDecimal(request.getQuantity()));
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
}