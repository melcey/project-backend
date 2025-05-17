package com.cs308.backend.dao;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales_manager_actions")
public class SalesManagerAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id")
    private Long actionId;

    // Many-to-one association
    @ManyToOne
    // The foreign key relationship between the `product_manager_actions` and `users` tables
    @JoinColumn(name = "sales_manager_id", referencedColumnName = "user_id", nullable = false)
    private User salesManager;

    @Column(name = "action_type", length = 255, nullable = false)
    private String actionType;

    @Column(name = "action_date", nullable = false)
    private LocalDateTime actionDate;

    @Column(name = "details")
    private String details;

    public SalesManagerAction() {
        this.actionDate = LocalDateTime.now();
    }

    public SalesManagerAction(User salesManager, String actionType, String details) {
        this.salesManager = salesManager;
        this.actionType = actionType;
        this.details = details;
        this.actionDate = LocalDateTime.now();
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public User getSalesManager() {
        return salesManager;
    }

    public void setSalesManager(User salesManager) {
        this.salesManager = salesManager;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public LocalDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SalesManagerAction [actionId=").append(actionId)
            .append(", salesManager=").append(salesManager)
            .append(", actionType=").append(actionType)
            .append(", actionDate=").append(actionDate)
            .append(", details=").append(details)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
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
        SalesManagerAction other = (SalesManagerAction) obj;
        if (actionId == null) {
            if (other.actionId != null)
                return false;
        } else if (!actionId.equals(other.actionId))
            return false;
        return true;
    }
}
