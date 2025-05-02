package com.cs308.backend.service;

import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cs308.backend.dao.CreditCard;
import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.OrderItem;
import com.cs308.backend.dao.Payment;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.PaymentRequest;
import com.cs308.backend.repo.CreditCardRepository;
import com.cs308.backend.repo.OrderRepository;
import com.cs308.backend.repo.PaymentRepository;
import com.cs308.backend.repo.ProductRepository;
import com.cs308.backend.repo.UserRepository;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    private final CreditCardRepository creditCardRepository;
    
    private final OrderRepository orderRepository;

    private final InvoiceService invoiceService;

    public PaymentService(PaymentRepository paymentRepository, CreditCardRepository creditCardRepository, OrderRepository orderRepository, InvoiceService invoiceService) {
        this.paymentRepository = paymentRepository;
        this.creditCardRepository = creditCardRepository;
        this.orderRepository = orderRepository;
        this.invoiceService = invoiceService;
    }

    @Transactional
    public Optional<Payment> processPayment(PaymentRequest paymentRequest, User user) {
        // If a user somehow tries to make a payment on behalf of another user
        if (!(paymentRequest.getUserId().equals(user.getId()))) {
            return Optional.empty();
        }

        // Find order
        Optional<Order> order = orderRepository.findById(paymentRequest.getOrderId());

        if (!(order.isPresent())) {
            return Optional.empty();
        }

        // Validate card (mock validation)
        if (!validateCard(paymentRequest)) {
            return Optional.empty();
        }

        Order orderToProcess = order.get();

        CreditCard creditCard = new CreditCard(
            user,
            paymentRequest.getCardHolderName(),
            paymentRequest.getExpiryMonth(),
            paymentRequest.getExpiryYear()
        );

        Optional<CreditCard> creditCardToSave = creditCardRepository.insertNewCard(creditCard, paymentRequest.getCardNumber(), paymentRequest.getCvv());

        if (!(creditCardToSave.isPresent())) {
            return Optional.empty();
        }

        // Create payment
        Payment payment = new Payment(
            orderToProcess,
            orderToProcess.getTotalPrice(),
            creditCardToSave.get()
        );

        try {
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
            e.printStackTrace();
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