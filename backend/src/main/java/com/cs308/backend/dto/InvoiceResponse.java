package com.cs308.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvoiceResponse {
    private Long id;
    private String invoiceNumber;
    private Long orderId;
    private Long paymentId;
    private LocalDateTime invoiceDate;
    private BigDecimal totalAmount;

    public InvoiceResponse() {
    }

    public InvoiceResponse(Long id, String invoiceNumber, Long orderId,
            Long paymentId, LocalDateTime invoiceDate,
            BigDecimal totalAmount) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}