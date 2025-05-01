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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "return_requests", schema = "public")
public class ReturnRequest {
    
    public enum Status {
        PENDING, APPROVED, REJECTED
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice = BigDecimal.ZERO;
    
    @Column(name = "reason")
    private String reason;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    
    @Column(name = "request_date")
    private LocalDateTime requestDate = LocalDateTime.now();
    
    @Column(name = "resolution_date")
    private LocalDateTime resolutionDate;
    
    @ManyToOne
    @JoinColumn(name = "resolved_by", referencedColumnName = "user_id")
    private User resolvedBy;

    // Constructors, getters, setters
    public ReturnRequest() {}

    public ReturnRequest(Order order, User user, Product product, int quantity, BigDecimal originalPrice, String reason) {
        this.order = order;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.reason = reason;
        this.requestDate = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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

    public User getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(User resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ReturnRequest [id=").append(id)
            .append(", order=").append(order)
            .append(", user=").append(user)
            .append(", product=").append(product)
            .append(", quantity=").append(quantity)
            .append(", originalPrice=").append(originalPrice)
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
        ReturnRequest other = (ReturnRequest) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }    
}