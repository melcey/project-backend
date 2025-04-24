package com.cs308.backend.repo;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cs308.backend.dao.CreditCard;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class CreditCardRepositoryImpl implements CreditCardRepositoryObj {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CreditCard> insertNewCard(CreditCard creditCard, String cardNumber, String cvv) {
        String sqlQuery = "INSERT INTO credit_cards (user_id, card_name_holder, card_number, expiry_month, expiry_year, cvv) VALUES (:user_id, :card_name_holder, crypt(:card_number, gen_salt('bf'))::bytea, :expiry_month, :expiry_year, crypt(:cvv, gen_salt('bf'))::bytea) RETURNING *";

        try {
            CreditCard newCreditCard = (CreditCard)entityManager.createNativeQuery(sqlQuery, CreditCard.class)
                .setParameter("user_id", creditCard.getUser().getId())
                .setParameter("card_name_holder", creditCard.getCardNameHolder())
                .setParameter("card_number", cardNumber)
                .setParameter("expiry_month", creditCard.getExpiryMonth())
                .setParameter("expiry_year", creditCard.getExpiryYear())
                .setParameter("cvv", cvv)
                .getSingleResult();

            entityManager.flush();
            entityManager.refresh(newCreditCard);

            return Optional.of(newCreditCard);

        }
        catch (Exception e) {
            return Optional.empty();
        }
    }
}
