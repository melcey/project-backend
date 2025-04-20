package com.cs308.backend.service;

import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.Payment;
import com.cs308.backend.dto.PaymentRequest;
import com.cs308.backend.repo.OrderRepository;
import com.cs308.backend.repo.PaymentRepository;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    
    private final OrderRepository orderRepository;

    private final InvoiceService invoiceService;

    private static final String ENCRYPTION_KEY = "zQUBdFwXE4a5PwzGL1XM14807r53Bd4RyoctkjNqIus=";

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository, InvoiceService invoiceService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.invoiceService = invoiceService;
    }

    @Transactional
    public Optional<Payment> processPayment(PaymentRequest paymentRequest) {
        // Find order
        Order order = orderRepository.findById(paymentRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Validate card (mock validation)
        if (!validateCard(paymentRequest)) {
            throw new RuntimeException("Invalid card details");
        }

        // Create payment
        Payment payment = new Payment(order, order.getTotalPrice());

        try {
            // Encrypt card details
            String cardDetails = String.format("%s|%s|%s|%s",
                    paymentRequest.getCardNumber(),
                    paymentRequest.getExpiryMonth(),
                    paymentRequest.getExpiryYear(),
                    paymentRequest.getCvv());

            payment.setEncryptedCardDetails(encryptCardDetails(cardDetails));

            // Process payment (mock processing)
            boolean success = processWithPaymentGateway(payment);

            if (success) {
                payment.setPaymentStatus("COMPLETED");
                payment = paymentRepository.save(payment);

                // Generate invoice
                invoiceService.generateInvoice(payment);

                return Optional.of(payment);
            } else {
                payment.setPaymentStatus("FAILED");
                return Optional.of(paymentRepository.save(payment));
            }
        } catch (Exception e) {
            payment.setPaymentStatus("FAILED");
            return Optional.of(paymentRepository.save(payment));
        }
    }

    private boolean validateCard(PaymentRequest request) {
        // Basic validation - implement more thorough validation in production
        return request.getCardNumber() != null &&
                request.getCardNumber().length() >= 13 &&
                request.getCardNumber().length() <= 19;
    }

    private byte[] encryptCardDetails(String cardDetails) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(cardDetails.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt card details", e);
        }
    }

    private boolean processWithPaymentGateway(Payment payment) {
        // Mock payment gateway integration
        // In production, integrate with actual payment gateway
        return true;
    }

    public Optional<Payment> findByOrderId(Long orderId) {
        Optional<Order> retrievedOrder = orderRepository.findById(orderId);

        // If there is no Order object in the Optional<> wrapper
        if (!(retrievedOrder.isPresent())) {
            return Optional.empty();
        }

        return paymentRepository.findByOrder(retrievedOrder.get());
    }
}