package com.cs308.backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.Invoice;
import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.Payment;


public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByOrder(Order order);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    Optional<Invoice> findByPayment(Payment payment);
}
