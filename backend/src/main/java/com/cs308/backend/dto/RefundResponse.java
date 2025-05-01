package com.cs308.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RefundResponse {
    private Long id;
    private Long returnRequestId;
    private BigDecimal amount;
    private LocalDateTime refundDate;
    private String status;

    public RefundResponse() {}

    public RefundResponse(Long id, Long returnRequestId, BigDecimal amount, LocalDateTime refundDate, String status) {
        this.id = id;
        this.returnRequestId = returnRequestId;
        this.amount = amount;
        this.refundDate = refundDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReturnRequestId() {
        return returnRequestId;
    }

    public void setReturnRequestId(Long returnRequestId) {
        this.returnRequestId = returnRequestId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RefundResponse [id=").append(id)
            .append(", returnRequestId=").append(returnRequestId)
            .append(", amount=").append(amount)
            .append(", refundDate=").append(refundDate)
            .append(", status=").append(status)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((returnRequestId == null) ? 0 : returnRequestId.hashCode());
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((refundDate == null) ? 0 : refundDate.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        RefundResponse other = (RefundResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (returnRequestId == null) {
            if (other.returnRequestId != null)
                return false;
        } else if (!returnRequestId.equals(other.returnRequestId))
            return false;
        if (amount == null) {
            if (other.amount != null)
                return false;
        } else if (!amount.equals(other.amount))
            return false;
        if (refundDate == null) {
            if (other.refundDate != null)
                return false;
        } else if (!refundDate.equals(other.refundDate))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        return true;
    }

    
}
