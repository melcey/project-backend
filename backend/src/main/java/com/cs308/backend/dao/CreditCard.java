package com.cs308.backend.dao;

import jakarta.persistence.*;

@Entity
@Table(name = "credit_cards")
public class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long cardId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "card_name_holder", nullable = false)
    private String cardNameHolder;

    @Column(name = "card_number", nullable = false)
    private byte[] cardNumber;

    @Column(name = "expiry_month")
    private Short expiryMonth;

    @Column(name = "expiry_year")
    private Short expiryYear;

    @Column(name = "cvv", nullable = false)
    private byte[] cvv;

    // Default constructor
    public CreditCard() {
    }

    // Constructor with all fields
    public CreditCard(User user, String cardNameHolder, byte[] cardNumber, Short expiryMonth, Short expiryYear,
            byte[] cvv) {
        this.user = user;
        this.cardNameHolder = cardNameHolder;
        this.cardNumber = cardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.cvv = cvv;
    }

    // Getters and Setters
    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCardNameHolder() {
        return cardNameHolder;
    }

    public void setCardNameHolder(String cardNameHolder) {
        this.cardNameHolder = cardNameHolder;
    }

    public byte[] getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(byte[] cardNumber) {
        this.cardNumber = cardNumber;
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

    public byte[] getCvv() {
        return cvv;
    }

    public void setCvv(byte[] cvv) {
        this.cvv = cvv;
    }
}