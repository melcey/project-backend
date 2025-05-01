package com.cs308.backend.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "refunds", schema = "public")
public class Refund {
    
    public enum Status {
        PROCESSING, COMPLETED, FAILED
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_id")
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "request_id")
    private ReturnRequest returnRequest;
    
    @Column(name = "amount")
    private BigDecimal amount;
    
    @Column(name = "refund_date")
    private LocalDateTime refundDate;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.PROCESSING;

    // Constructors, getters, setters
    public Refund() {}

    public Refund(ReturnRequest returnRequest, BigDecimal amount) {
        this.returnRequest = returnRequest;
        this.amount = amount;
        this.refundDate = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReturnRequest getReturnRequest() {
        return returnRequest;
    }

    public void setReturnRequest(ReturnRequest returnRequest) {
        this.returnRequest = returnRequest;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(LocalDateTime refundDate) {
        this.refundDate = refundDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}