package com.cs308.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cs308.backend.dao.CreditCard;
import com.cs308.backend.dao.User;

import java.util.List;
import java.util.Optional;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    // Find all credit cards for a specific user
    List<CreditCard> findByUser(User user);

    // Find a specific card by id
    Optional<CreditCard> findById(Long cardId);

    // Find cards by card holder name (exact match)
    List<CreditCard> findByCardNameHolder(String cardNameHolder);

    // Find cards by card holder name (containing)
    List<CreditCard> findByCardNameHolderContaining(String cardNameHolder);

    // Find by expiry month and year
    List<CreditCard> findByExpiryMonthAndExpiryYear(Short expiryMonth, Short expiryYear);

    // Find cards expiring in a specific month
    List<CreditCard> findByExpiryMonth(Short expiryMonth);

    // Find cards expiring in a specific year
    List<CreditCard> findByExpiryYear(Short expiryYear);

    // Delete all cards for a specific user
    void deleteByUser(User user);
}