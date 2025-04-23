package com.cs308.backend.repo;

import java.util.Optional;

import com.cs308.backend.dao.CreditCard;

public interface CreditCardRepositoryObj {
    Optional<CreditCard> insertNewCard(CreditCard creditCard, String cardNumber, String cvv);
}
