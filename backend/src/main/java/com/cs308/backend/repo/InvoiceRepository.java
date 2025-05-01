package com.cs308.backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.Invoice;
import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.Payment;
import java.util.List;
import java.time.LocalDateTime;



public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByOrder(Order order);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    Optional<Invoice> findByPayment(Payment payment);

    List<Invoice> findByInvoiceDate(LocalDateTime invoiceDate);

    List<Invoice> findByInvoiceDateBefore(LocalDateTime invoiceDate);
    
    List<Invoice> findByInvoiceDateAfter(LocalDateTime invoiceDate);

    List<Invoice> findByInvoiceDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
