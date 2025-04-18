package com.cs308.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cs308.backend.dao.Invoice;
import com.cs308.backend.dao.Order;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByOrder(Order order);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
