package com.cs308.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cs308.backend.dao.Payment;
import com.cs308.backend.dao.Order;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);
}
