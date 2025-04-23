package com.cs308.backend.dto;

public class PaymentRequest {
    private Long orderId;
    private Long userId;
    private String cardNumber;
    private String cardHolderName;
    private Short expiryMonth;
    private Short expiryYear;
    private String cvv;

    // Default constructor
    public PaymentRequest() {}

    public PaymentRequest(Long orderId, Long userId, String cardNumber, String cardHolderName, Short expiryMonth,
            Short expiryYear, String cvv) {
        this.orderId = orderId;
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Short getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(Short expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public Short getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(Short expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PaymentRequest [orderId=").append(orderId)
            .append(", userId=").append(userId)
            .append(", cardNumber=").append(cardNumber)
            .append(", cardHolderName=").append(cardHolderName)
            .append(", expiryMonth=").append(expiryMonth)
            .append(", expiryYear=").append(expiryYear)
            .append(", cvv=").append(cvv)
            .append("]");


        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
        result = prime * result + ((cardHolderName == null) ? 0 : cardHolderName.hashCode());
        result = prime * result + ((expiryMonth == null) ? 0 : expiryMonth.hashCode());
        result = prime * result + ((expiryYear == null) ? 0 : expiryYear.hashCode());
        result = prime * result + ((cvv == null) ? 0 : cvv.hashCode());
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
        PaymentRequest other = (PaymentRequest) obj;
        if (orderId == null) {
            if (other.orderId != null)
                return false;
        } else if (!orderId.equals(other.orderId))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (cardNumber == null) {
            if (other.cardNumber != null)
                return false;
        } else if (!cardNumber.equals(other.cardNumber))
            return false;
        if (cardHolderName == null) {
            if (other.cardHolderName != null)
                return false;
        } else if (!cardHolderName.equals(other.cardHolderName))
            return false;
        if (expiryMonth == null) {
            if (other.expiryMonth != null)
                return false;
        } else if (!expiryMonth.equals(other.expiryMonth))
            return false;
        if (expiryYear == null) {
            if (other.expiryYear != null)
                return false;
        } else if (!expiryYear.equals(other.expiryYear))
            return false;
        if (cvv == null) {
            if (other.cvv != null)
                return false;
        } else if (!cvv.equals(other.cvv))
            return false;
        return true;
    }
}